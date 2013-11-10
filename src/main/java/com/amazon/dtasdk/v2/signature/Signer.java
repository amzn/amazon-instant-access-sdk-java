/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.dtasdk.v2.signature;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazon.dtasdk.v2.utils.Clock;

/**
 * This class either signs or verifies the signature on a @link(Request) for Signature version 1.
 * 
 * The signature algorithm is based off of AwsAuthV4 (https://w.amazon.com/index.php/AWSAuth/Signature/V4). However it
 * has minor changes to support two way communication between Amazon and External Parties.
 */
public class Signer {
    private static final Log log = LogFactory.getLog(Signer.class);

    public static final String ALGORITHM_HEADER = "DTA1-HMAC-SHA256";
    public static final String X_AMZ_DATE_HEADER = "x-amz-date";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Specified by the AWSAuthv4 to be a 15 minute window either side that we allow. We *can* tighten this up.
    public static final int TIME_TOLERANCE = 15 * 1000 * 60;

    private Clock clock;
    private AuthenticationHeaderParser authenticationHeaderParser;
    protected static final String ALGORITHM = "HmacSHA256";

    /** The default encoding to use when URL encoding */
    protected static final String DEFAULT_ENCODING = "UTF-8";

    protected ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
            return dateTimeFormat;
        }
    };

    protected ThreadLocal<SimpleDateFormat> dateStampFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat dateStampFormat = new SimpleDateFormat("yyyyMMdd");
            dateStampFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
            return dateStampFormat;
        }
    };

    /**
     * Creates a complete Signer ready to be used.
     */
    public Signer() {
        this.clock = new Clock();
        this.authenticationHeaderParser = new AuthenticationHeaderParser();
    };

    /**
     * Creates a Signer with a clock and a parser. This is used for testing purposes.
     * 
     * @param clock
     *            the clock to use to get the current time
     * @param authenticationHeaderParser
     *            the header parser
     */
    protected Signer(Clock clock, AuthenticationHeaderParser authenticationHeaderParser) {
        this.clock = clock;
        this.authenticationHeaderParser = authenticationHeaderParser;
    }

    /**
     * Verifies the request against a credential.
     * 
     * @param request
     *            the request to verify. Typically created from an HttpServletRequest.
     * @param credential
     *            the credential used to verify the request.
     * @return true if the request validates. false for any other reason except a SigningException.
     * @throws SigningException
     *             if there was a problem with the underlying crypto.
     */
    public boolean verify(Request request, Credential credential) throws SigningException {
        CredentialStore store = new CredentialStore();
        store.add(credential);
        return verify(request, store);
    }

    /**
     * Verifies the request against a credential store.
     * 
     * @param request
     *            the request to verify. Typically created from an HttpServletRequest.
     * @param credentialStore
     *            the credential store used to verify the request.
     * @return true if the request validates. false for any other reason except a SigningException.
     * @throws SigningException
     *             if there was a problem with the underlying crypto.
     */
    public boolean verify(Request request, CredentialStore credentialStore) throws SigningException {
        Date now = clock.now();
        String strDate = dateStampFormat.get().format(now);
        String dateTime = request.getHeader(X_AMZ_DATE_HEADER);

        if (dateTime == null) {
            return false;
        }

        // Fail if the Authentication header is not found
        String actualAuthorization = request.getHeader(AUTHORIZATION_HEADER);
        if (actualAuthorization == null || "".equals(actualAuthorization)) {
            return false;
        }

        request.removeHeader(AUTHORIZATION_HEADER);

        // Clear any header that isn't in the list of signed signedHeaders
        AuthenticationHeader authenticationHeader = authenticationHeaderParser.parse(actualAuthorization);

        if (authenticationHeader == null) {
            return false;
        }

        String[] signedHeaders = authenticationHeader.getSignedHeaders().split(";");

        removeUnsignedHeaders(request, signedHeaders);

        try {
            Date dateOfRequest = dateTimeFormat.get().parse(dateTime);
            long delta = dateOfRequest.getTime() - now.getTime();
            if (Math.abs(delta) > TIME_TOLERANCE) {
                return false;
            }
        } catch (ParseException pe) {
            log.warn(String.format("Could not parse request date of [%s]", dateTime), pe);
            return false;
        }

        // The credential info should follow this pattern: KEYID/DATE
        String[] credentialInfo = authenticationHeader.getCredential().split("/");

        if (credentialInfo.length < 2) {
            log.warn("Could not get the credential information from the authorization header");
            return false;
        }

        Credential credential;
        try {
            credential = credentialStore.get(credentialInfo[0]);
        } catch (CredentialNotFoundException e) {
            log.warn(String.format("Public key not found [%s]", credentialInfo[0]));
            return false;
        }

        byte[] timedKey = sign(strDate, credential.getSecretKey());
        String canonicalRequest = getCanonicalRequest(request);
        String stringToSign = getStringToSign(ALGORITHM_HEADER, dateTime, "", canonicalRequest);
        String signature = BinaryUtils.toHex(sign(stringToSign, timedKey));

        String computedAuthorization = String.format("%s SignedHeaders=%s, Credential=%s/%s, Signature=%s",
                ALGORITHM_HEADER, getSignedHeadersString(request), credential.getPublicKey(), strDate, signature);

        if (computedAuthorization.equals(actualAuthorization)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Trims out any headers that were not signed from the request to allow easier calculation of the signature for
     * verification.
     * 
     * @param request
     *            the request to remove headers from.
     * @param signedHeaders
     *            the headers that should remain.
     */
    protected void removeUnsignedHeaders(Request request, String[] signedHeaders) {
        // have to copy the header name set to avoid concurrent modification exception.
        ArrayList<String> headersList = new ArrayList<String>();
        headersList.addAll(request.getHeaderNames());

        for (String header : headersList) {
            boolean match = false;
            for (String signedHeader : signedHeaders) {
                if (signedHeader.equalsIgnoreCase(header)) {
                    match = true;
                    break;
                }
            }

            if (match) {
                continue;
            }
            request.removeHeader(header);
        }
    }

    /**
     * Signs the request and adds the authentication headers (Authentication & x-amz-date).
     * 
     * @param request
     *            the request to sign.
     * @param credential
     *            the credential to use when signing.
     * @throws SigningException
     *             if there was a problem with the underlying crypto.
     */
    public void sign(Request request, Credential credential) throws SigningException {
        Date now = clock.now();
        String strDate = dateStampFormat.get().format(now);
        String dateTime = dateTimeFormat.get().format(now);

        request.setHeader(X_AMZ_DATE_HEADER, dateTime);

        byte[] timedKey = sign(strDate, credential.getSecretKey());

        // Remove the Authorization header from the request, since it could have been set if sign() was previously
        // called on this request.
        request.removeHeader(AUTHORIZATION_HEADER);

        String canonicalRequest = getCanonicalRequest(request);

        String stringToSign = getStringToSign(ALGORITHM_HEADER, dateTime, "", canonicalRequest);

        String signature = BinaryUtils.toHex(sign(stringToSign, timedKey));

        StringBuilder builder = new StringBuilder();
        builder.append(ALGORITHM_HEADER).append(" ");
        builder.append("SignedHeaders=").append(getSignedHeadersString(request)).append(", ");
        builder.append("Credential=").append(credential.getPublicKey()).append("/").append(strDate).append(", ");
        builder.append("Signature=").append(signature);

        request.setHeader("Authorization", builder.toString());
    }

    /**
     * Calculates and returns the content hash of the request. Per AwsAuthV4, we don't sign the body, we sign the hash
     * of the body. This allows validation to be offloaded to other servers more easily.
     * 
     * @param request
     *            the request to get the content from
     * @return a String representing the hexidecimal hash of the body
     * @throws SigningException
     *             if there was a problem with the underlying crypto.
     */
    protected String getContentHash(Request request) throws SigningException {
        String body = request.getBody();
        if (body == null) {
            body = "";
        }
        return BinaryUtils.toHex(hash(body));
    }

    protected byte[] hash(String text) throws SigningException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(DEFAULT_ENCODING));
            return md.digest();
        } catch (UnsupportedEncodingException e) {
            throw new SigningException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new SigningException(e);
        }
    }

    /**
     * Returns a canonical string representation of the request headers
     * 
     * @param request
     *            to get the headers from.
     * @return a string of the form header_name1:header_value1 \n header_name2:header_value2
     */
    protected String getCanonicalizedHeaderString(Request request) {
        List<String> sortedHeaders = getSortedHeaders(request);

        StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            buffer.append(header.toLowerCase().replaceAll("\\s+", " ") + ":"
                    + request.getHeader(header).replaceAll("\\s+", " "));
            buffer.append("\n");
        }

        return buffer.toString();
    }

    /**
     * Returns a string with all the signed headers names found in the request separated by ';'.
     * 
     * @param request
     *            to get the headers from.
     * @return a string of the form HEADER_NAME1;HEADER_NAME2;HEADER_NAME3 to sign
     */
    protected String getSignedHeadersString(Request request) {
        List<String> sortedHeaders = getSortedHeaders(request);

        StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            if (buffer.length() > 0) {
                buffer.append(";");
            }
            buffer.append(header.toLowerCase());
        }

        return buffer.toString();
    }

    protected String getCanonicalizedResourcePath(String resourcePath) {
        if (resourcePath == null || resourcePath.length() == 0) {
            return "/";
        } else {
            String value = HttpUtils.urlEncode(resourcePath, true);
            if (value.startsWith("/")) {
                return value;
            } else {
                return "/".concat(value);
            }
        }
    }

    protected List<String> getSortedHeaders(Request request) {
        List<String> sortedHeaders = new ArrayList<String>(request.getHeaderNames());
        Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);
        return sortedHeaders;
    }

    /**
     * Returns the canonical representation of the request. The canonical request is of the form:
     * 
     * <pre>
     * METHOD
     * CANONICAL_PATH
     * CANONICAL_QUERY_STRING
     * CANONICAL_HEADER_STRING
     * SIGNED_HEADERS
     * CONTENT_HASH
     * </pre>
     * 
     * Which for a get request to http://amazon.com/path would be:
     * 
     * <pre>
     * GET
     * /path
     * 
     * x-amz-date
     * 20110909T112349Z
     * CONTENT_HASH
     * </pre>
     * 
     * @param request
     *            the request to canonicalize.
     * @return the canonical request.
     * @throws SigningException
     *             if there was a problem with the underlying crypto.
     */
    protected String getCanonicalRequest(Request request) throws SigningException {
        /* This would url-encode the resource path for the first time */

        URL url;
        try {
            url = new URL(request.getUrl());
        } catch (MalformedURLException murle) {
            log.warn(String.format("Unable to parse url [%s]", request.getUrl()));
            throw new SigningException(murle);
        }

        String canonicalRequest = request.getMethod().toString() + "\n" + getCanonicalizedResourcePath(url.getPath())
                + "\n" + "\n"
                + // Query String would go here
                getCanonicalizedHeaderString(request) + "\n" + getSignedHeadersString(request) + "\n"
                + getContentHash(request);
        log.debug("AWS4 Canonical Request: '\"" + canonicalRequest + "\"'");
        return canonicalRequest;
    }

    protected String getStringToSign(String algorithm, String dateTime, String scope, String canonicalRequest)
            throws SigningException {
        String stringToSign = algorithm + "\n" + dateTime + "\n" + scope + "\n"
                + BinaryUtils.toHex(hash(canonicalRequest));
        log.debug("AWS4 String to Sign: '\"" + stringToSign + "\"");
        return stringToSign;
    }

    protected final byte[] sign(String stringToSign, byte[] key) throws SigningException {
        return sign(stringToSign.getBytes(), key);
    }

    protected final byte[] sign(String data, String key) throws SigningException {
        return sign(data.getBytes(), key.getBytes());
    }

    protected final byte[] sign(byte[] dataBytes, byte[] keyBytes) throws SigningException {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(keyBytes, ALGORITHM));
            return mac.doFinal(dataBytes);
        } catch (NoSuchAlgorithmException nsae) {
            throw new SigningException(nsae);
        } catch (InvalidKeyException ike) {
            throw new SigningException(ike);
        }
    }
}

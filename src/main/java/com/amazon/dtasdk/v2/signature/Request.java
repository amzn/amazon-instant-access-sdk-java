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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Represents an HTTP request. This is used to for both signing and verifying a request to be signed.
 */
public class Request {
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (compatible; Amazon Instant Access/1.0";
    private String url;
    private Method method;
    private String body;
    private Map<String, String> headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    private String userAgent;

    /**
     * Creates a Request from an HttpServletRequest. Useful for verifying the signature of a request.
     * 
     * NOTE: This consumes the body of the request which can cause issues when you try and read it again.
     * 
     * @param httpServletRequest
     *            the HttpServletRequest to copy
     * @throws IOException
     *             on invalid url or body copying
     */
    public Request(HttpServletRequest httpServletRequest) throws IOException {
        url = getFullURL(httpServletRequest);
        method = Method.valueOf(httpServletRequest.getMethod());

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, httpServletRequest.getHeader(name));
        }
        headers.put(CONTENT_TYPE_HEADER, httpServletRequest.getContentType());
        body = IOUtils.toString(httpServletRequest.getInputStream());
    }

    public Request(String url, Method method, String contentType) {
        validateUrl(url);
        this.url = url;
        this.method = method;
        this.headers.put(CONTENT_TYPE_HEADER, contentType);
        this.userAgent = DEFAULT_USER_AGENT;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * An enumeration of HTTP methods supported
     */
    public enum Method {
        POST,
        GET
    }

    /**
     * @return the full url of the request
     */
    public String getUrl() {
        return url;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void removeHeader(String headerName) {
        headers.remove(headerName);
    }

    public Method getMethod() {
        return method;
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    private void validateUrl(String inputUrl) {
        if (inputUrl == null) {
            throw new IllegalArgumentException("inputUrl cannot be null");
        }
        try {
            new URL(inputUrl).toURI();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("inputUrl does not resolve to a valid URL", e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("inputUrl does not resolve to a valid URI", e);
        }
    }

    private String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (StringUtils.isEmpty(queryString)) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}

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

/**
 * Bean class representing the authentication header, which looks like
 * 
 * <pre>
 * Authentication: ALGORITHM Credential=CREDENTIAL, SignedHeaders=SIGNED_HEADERS, Signature=SIGNATURE
 * </pre>
 * 
 * Where:
 * ALGORITHM := The signing algorithm used for the credential, ex. DTAv1-SHA-256
 * CREDENTIAL := KEYID/DATE.
 * SIGNED_HEADERS := lower cased header names sorted by byte order joined with semicolons.
 * SIGNATURE := The signature calculated by the signing algorithm.
 * KEYID := The public id for the sceret key used to calculate the signature.
 * DATE := The date the message was signed in YYMMDD format. This is used to generate the daily key.
 */
public class AuthenticationHeader {
    private String algorithm;
    private String credential;
    private String signedHeaders;
    private String signature;

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public void setSignedHeaders(String signedHeaders) {
        this.signedHeaders = signedHeaders;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getCredential() {
        return credential;
    }

    public String getSignedHeaders() {
        return signedHeaders;
    }

    public String getSignature() {
        return signature;
    }
}

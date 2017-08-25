/*
 * Copyright 2017-2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazon.dtasdk.signature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses an AuthenticationHeader from a header value string.
 * 
 * Pattern: ALGORITHM SignedHeaders=x;y;z, Credential=XXX, Signature=BIGSHA
 * Example: DTA1-HMAC-SHA256 SignedHeaders=aaa;content-type;x-amz-date;zzz, Credential=KEYID/20110909,
 * Signature=87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628
 */
public class AuthenticationHeaderParser {
    private static final Pattern pattern = Pattern
            .compile("(\\S+) SignedHeaders=(\\S+), Credential=(\\S+), Signature=([\\S&&[^,]]+)");

    public AuthenticationHeader parse(String headerString) {
        Matcher match = pattern.matcher(headerString);
        AuthenticationHeader header = new AuthenticationHeader();

        if (!match.find()) {
            return null;
        }

        header.setAlgorithm(match.group(1));
        header.setSignedHeaders(match.group(2));
        header.setCredential(match.group(3));
        header.setSignature(match.group(4));

        return header;
    }
}

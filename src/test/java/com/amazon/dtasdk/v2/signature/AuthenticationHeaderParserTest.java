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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AuthenticationHeaderParserTest {
    @Test
    public void singleHeaderAuthentication() {
        String value = "DTA1-HMAC-SHA256 " + "SignedHeaders=aaa;content-type;x-amz-date;zzz, "
                + "Credential=KEYID/20110909, "
                + "Signature=87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628";

        AuthenticationHeader result = new AuthenticationHeaderParser().parse(value);

        assertEquals("DTA1-HMAC-SHA256", result.getAlgorithm());
        assertEquals("KEYID/20110909", result.getCredential());
        assertEquals("aaa;content-type;x-amz-date;zzz", result.getSignedHeaders());
        assertEquals("87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628", result.getSignature());
    }

    @Test
    public void singleHeaderAuthentication2() {
        String value = "DTA1-HMAC-SHA256 "
                + "SignedHeaders=content-type;x-amz-date;x-amz-dta-version;x-amz-request-id, "
                + "Credential=367caa91-cde5-48f2-91fe-bb95f546e9f0/20131207, "
                + "Signature=6fe5d5bbf4acda9b0f47f66db3ad8f23a33117ee52b45ae69983bec0b50550fe";

        AuthenticationHeader result = new AuthenticationHeaderParser().parse(value);

        assertEquals("DTA1-HMAC-SHA256", result.getAlgorithm());
        assertEquals("367caa91-cde5-48f2-91fe-bb95f546e9f0/20131207", result.getCredential());
        assertEquals("content-type;x-amz-date;x-amz-dta-version;x-amz-request-id", result.getSignedHeaders());
        assertEquals("6fe5d5bbf4acda9b0f47f66db3ad8f23a33117ee52b45ae69983bec0b50550fe", result.getSignature());
    }

    @Test
    public void invalidHeader() {
        assertNull(new AuthenticationHeaderParser().parse("DAT"));
    }

    @Test
    public void futureCompatibilityForMultipleHeaders() {
        String value = "DTA1-HMAC-SHA256 " + "SignedHeaders=aaa;content-type;x-amz-date;zzz, "
                + "Credential=KEYID/20110909, "
                + "Signature=87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628, "
                + "Credential=KEYID2/20110909, "
                + "Signature=OTHERSIGNATUREalskdjfasldkjf234lkj234lkjalkj234lkj324lkj2345lkj2";

        AuthenticationHeader result = new AuthenticationHeaderParser().parse(value);

        assertEquals("DTA1-HMAC-SHA256", result.getAlgorithm());
        assertEquals("KEYID/20110909", result.getCredential());
        assertEquals("aaa;content-type;x-amz-date;zzz", result.getSignedHeaders());
        assertEquals("87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628", result.getSignature());
    }
}

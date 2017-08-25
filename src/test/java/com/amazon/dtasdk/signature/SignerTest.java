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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import com.amazon.dtasdk.utils.Clock;

public class SignerTest {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String X_AMZ_DATE_HEADER = "x-amz-date";

    // By design several of these first tests only verify that signatures calculate to an externally verified signature.
    // The more interesting and robust tests are the round trip ones.

    @Test
    public void validSignature() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");

        signer.sign(request, credential);

        assertEquals("20110909T233600Z", request.getHeader("x-amz-date"));
        assertEquals("DTA1-HMAC-SHA256 " + "SignedHeaders=content-type;x-amz-date, " + "Credential=KEYID/20110909, "
                + "Signature=4d2f81ea2cf8d6963f8176a22eec4c65ae95c63502326a7c148686da7d50f47e",
                request.getHeader("Authorization"));
    }

    @Test
    public void validSignatureAfterSigningTwice() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date1 = getDate(2011, 9, 9, 01, 01, 0);
        Date date2 = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date1);
        expect(clock.now()).andReturn(date2);
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");

        signer.sign(request, credential);
        signer.sign(request, credential);

        assertEquals("20110909T233600Z", request.getHeader("x-amz-date"));
        assertEquals("DTA1-HMAC-SHA256 " + "SignedHeaders=content-type;x-amz-date, " + "Credential=KEYID/20110909, "
                + "Signature=4d2f81ea2cf8d6963f8176a22eec4c65ae95c63502326a7c148686da7d50f47e",
                request.getHeader("Authorization"));
    }

    @Test
    public void validSignatureNullBody() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody(null);

        signer.sign(request, credential);

        assertEquals("20110909T233600Z", request.getHeader("x-amz-date"));
        assertEquals("DTA1-HMAC-SHA256 " + "SignedHeaders=content-type;x-amz-date, " + "Credential=KEYID/20110909, "
                + "Signature=d3042ffc41e6456535558faa130655a1c957263467e78d4485e70884b49ea52b",
                request.getHeader("Authorization"));
    }

    @Test
    public void additonalHeadersAreSigned() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");
        request.setHeader("aaa", "aaa");
        request.setHeader("zzz", "zzz");

        signer.sign(request, credential);

        assertEquals("20110909T233600Z", request.getHeader("x-amz-date"));
        assertEquals("DTA1-HMAC-SHA256 " + "SignedHeaders=aaa;content-type;x-amz-date;zzz, "
                + "Credential=KEYID/20110909, "
                + "Signature=16ec5ffa0e33e8ec8f87f14bb5fd8a03545dbffe99eb3a89f5de450e791ef262",
                request.getHeader("Authorization"));
    }

    @Test
    public void verifyNoAuthorization() throws SigningException {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        Signer signer = new Signer();

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");

        assertFalse(signer.verify(verificationRequest, credential));
    }

    @Test
    public void verifyBadAuthrorization() throws SigningException {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        Signer signer = new Signer();

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setHeader(AUTHORIZATION_HEADER, "THIS IS AN INVALID AUTHORZIATION HEADER");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");

        assertFalse(signer.verify(verificationRequest, credential));
    }

    @Test
    public void verifyNoDate() throws SigningException {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        Signer signer = new Signer();

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setHeader(AUTHORIZATION_HEADER, "");

        assertFalse(signer.verify(verificationRequest, credential));
    }

    @Test
    public void roundTrip() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");
        request.setHeader("aaa", "aaa");
        request.setHeader("zzz", "zzz");

        signer.sign(request, credential);

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        // NOTE: HttpServletRequest always seems to put a trailing slash on bare urls so the test mimics this
        Request verificationRequest = new Request("http://amazon.com/", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest.setHeader(AUTHORIZATION_HEADER, authorization);

        assertTrue(signer.verify(verificationRequest, credential));
    }

    @Test
    public void roundTripWithCredentialStore() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");
        request.setHeader("aaa", "aaa");
        request.setHeader("zzz", "zzz");

        signer.sign(request, credential);

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        // NOTE: HttpServletRequest always seems to put a trailing slash on bare urls so the test mimics this
        Request verificationRequest = new Request("http://amazon.com/", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest.setHeader(AUTHORIZATION_HEADER, authorization);

        CredentialStore store = new CredentialStore();
        store.load("AUXKEY AUXKEYID\nSECRETKEY KEYID\n");

        assertTrue(signer.verify(verificationRequest, store));
    }

    @Test
    public void ignoresExtraHeaders() throws SigningException {
        Clock clock = createMock(Clock.class);
        Date date = getDate(2011, 9, 9, 23, 36, 0);
        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request request = new Request("http://amazon.com", Request.Method.GET, "application/json");
        request.setBody("body");

        signer.sign(request, credential);

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        // NOTE: HttpServletRequest always seems to put a trailing slash on bare urls so the test mimics this
        Request verificationRequest = new Request("http://amazon.com/", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest.setHeader(AUTHORIZATION_HEADER, authorization);
    }

    @Test
    public void fiveMinutesAfterValidates() throws SigningException {
        Clock clock = createMock(Clock.class);
        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Date date = getDate(2011, 9, 9, 23, 36 + 5, 0); // Go 5 minutes after

        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest
                .setHeader(
                        AUTHORIZATION_HEADER,
                        "DTA1-HMAC-SHA256 SignedHeaders=aaa;content-type;x-amz-date;zzz, Credential=KEYID/20110909, Signature=16ec5ffa0e33e8ec8f87f14bb5fd8a03545dbffe99eb3a89f5de450e791ef262");

        assertTrue(signer.verify(verificationRequest, credential));
    }

    @Test
    public void fiveMinutesBeforeValidates() throws SigningException {
        Clock clock = createMock(Clock.class);
        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Date date = getDate(2011, 9, 9, 23, 36 - 5, 0); // Go 5 minutes before

        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest
                .setHeader(
                        AUTHORIZATION_HEADER,
                        "DTA1-HMAC-SHA256 SignedHeaders=aaa;content-type;x-amz-date;zzz, Credential=KEYID/20110909, Signature=16ec5ffa0e33e8ec8f87f14bb5fd8a03545dbffe99eb3a89f5de450e791ef262");

        assertTrue(signer.verify(verificationRequest, credential));
    }

    @Test
    public void after15MinuteWindowFailsValidation() throws SigningException {
        Clock clock = createMock(Clock.class);
        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Date date = getDate(2011, 9, 9, 23, 36 + 15, 1); // Go 15 Minutes after

        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest
                .setHeader(
                        AUTHORIZATION_HEADER,
                        "DTA1-HMAC-SHA256 SignedHeaders=aaa;content-type;x-amz-date;zzz, Credential=KEYID/20110909, Signature=87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628");

        assertFalse(signer.verify(verificationRequest, credential));
    }

    @Test
    public void before15MinuteWindowFailsValidation() throws SigningException {
        Clock clock = createMock(Clock.class);
        Signer signer = new Signer(clock, new AuthenticationHeaderParser());

        Date date = getDate(2011, 9, 9, 23, 36 - 16, 59); // Go 15 Minutes Before

        expect(clock.now()).andReturn(date).anyTimes();
        replay(clock);

        Credential credential = new Credential("SECRETKEY", "KEYID");

        Request verificationRequest = new Request("http://amazon.com", Request.Method.GET, "application/json");
        verificationRequest.setBody("body");
        verificationRequest.setHeader("aaa", "aaa");
        verificationRequest.setHeader("zzz", "zzz");
        verificationRequest.setHeader(X_AMZ_DATE_HEADER, "20110909T233600Z");
        verificationRequest
                .setHeader(
                        AUTHORIZATION_HEADER,
                        "DTA1-HMAC-SHA256 SignedHeaders=aaa;content-type;x-amz-date;zzz, Credential=KEYID/20110909, Signature=87729cb3475859a18b5d9cead0bba82f0f56a85c2a13bed3bc229c6c35e06628");

        assertFalse(signer.verify(verificationRequest, credential));
    }

    private Date getDate(int year, int month, int day, int hour, int minute, int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Z"));
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, second);
        return calendar.getTime();
    }

}

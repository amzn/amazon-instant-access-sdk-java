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
package com.amazon.dtasdk.connection;

import com.amazon.dtasdk.signature.Credential;
import com.amazon.dtasdk.signature.Request;
import com.amazon.dtasdk.signature.Signer;
import com.amazon.dtasdk.signature.SigningException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

@Deprecated
public class SignatureVerificationTest {

    private static MockHandler handler;
    private static TestServer server;
    private static HttpClient httpClient;
    private HttpClient client;
    private Signer signer;

    static class MockHandler extends AbstractHandler {

        private Credential credential;
        private Signer signer = new Signer();
        private Exception exception;

        @Override
        public void handle(String s, org.eclipse.jetty.server.Request jettyRequest,
                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException,
                ServletException {
            Request request = new Request(httpServletRequest);

            try {
                if (!signer.verify(request, this.credential)) {
                    httpServletResponse.setStatus(403);
                    jettyRequest.setHandled(true);
                    return;
                }
            } catch (SigningException e) {
                this.exception = e;
                throw new IOException(e);
            }

            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write("success");
            jettyRequest.setHandled(true);
        }

        public void setCredential(Credential credential) {
            this.credential = credential;
        }

        public void clear() {
            exception = null;
        }

        public Exception getException() {
            return exception;
        }
    }

    @BeforeClass
    public static void startServer() throws Exception {
        handler = new MockHandler();
        server = new TestServer(handler).start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    private String serverUrl() {
        return "http://localhost:" + server.getPort();
    }

    private void setDefaultHeaders(Request request) {
        request.setHeader("x-amz-request-id", "0A49CE4060975EAC");
        request.setHeader("x-amz-customer-id", "ABCDCUSTID");
        request.setHeader("x-amz-dta-version", "1");
    }

    private void copyHeaders(Request request, HttpMethod method) {
        for (String headerName : request.getHeaderNames()) {
            method.setRequestHeader(headerName, request.getHeader(headerName));
        }
    }

    @Before
    public void before() throws KeyManagementException, NoSuchAlgorithmException {
        client = new HttpClient();
        signer = new Signer();
    }

    @Test
    public void roundTripServer_withBareUrl() throws Exception {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        handler.setCredential(credential);

        Request request = new Request(serverUrl(), Request.Method.GET, "");
        setDefaultHeaders(request);
        signer.sign(request, credential);

        HttpMethod method = new GetMethod(request.getUrl());
        copyHeaders(request, method);

        assertEquals(200, client.executeMethod(method));
    }

    @Test
    public void roundTrip_withPath() throws Exception {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        handler.setCredential(credential);

        Request request = new Request(serverUrl() + "/path/to/servlet", Request.Method.POST, "");
        setDefaultHeaders(request);
        request.setBody("{\"json\": \"value\"}");
        signer.sign(request, credential);

        PostMethod method = new PostMethod(request.getUrl());
        copyHeaders(request, method);
        method.setRequestBody(request.getBody());

        assertEquals(200, client.executeMethod(method));
    }

    @Test
    public void roundTrip_NoAuth() throws Exception {
        Credential credential = new Credential("SECRETKEY", "KEYID");
        handler.setCredential(credential);

        PostMethod method = new PostMethod(serverUrl() + "/path/to/servlet");
        method.setRequestBody("{\"json\": \"value\"}");

        assertEquals(403, client.executeMethod(method));
    }
}

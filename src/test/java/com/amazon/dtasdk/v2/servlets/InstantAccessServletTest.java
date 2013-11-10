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
package com.amazon.dtasdk.v2.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.messages.InstantAccessOperationValue;
import com.amazon.dtasdk.v2.serialization.messages.InstantAccessResponse;
import com.amazon.dtasdk.v2.serialization.serializer.SerializationException;
import com.amazon.dtasdk.v2.signature.Credential;
import com.amazon.dtasdk.v2.signature.CredentialStore;
import com.amazon.dtasdk.v2.signature.Request;
import com.amazon.dtasdk.v2.signature.Request.Method;
import com.amazon.dtasdk.v2.signature.Signer;
import com.amazon.dtasdk.v2.signature.SigningException;

/**
 * Test class for InstantAccessServlet.
 */
public class InstantAccessServletTest {

    private static final String RESPONSE = "OK";

    private class InstantAccessServletImpl extends InstantAccessServlet {

        private final CredentialStore store;

        public InstantAccessServletImpl(CredentialStore store) {
            this.store = store;
        }

        @Override
        public CredentialStore getCredentialStore() {
            return store;
        }

        @Override
        public InstantAccessResponse<?> processOperation(InstantAccessOperationValue instantAccessOperationValue,
                String requestBody) throws SerializationException {
            return new InstantAccessResponse<String>() {
                {
                    setResponse(RESPONSE);
                }
            };
        }
    }

    @Test
    public void testDoPost() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        InstantAccessServlet servlet = new InstantAccessServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                                 "\"operation\": \"GetUserId\"," +
                                 "\"infoField1\": \"nobody@amazon.com\"," +
                                 "\"infoField2\": \"amazon\"" +
                             "}";
        // @formatter:on

        mockRequest(request, bodyContent, credential);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        EasyMock.expect(response.getOutputStream()).andReturn(new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
        });

        response.setStatus(HttpServletResponse.SC_OK);

        EasyMock.replay(request, response);

        servlet.doPost(request, response);

        EasyMock.verify(request, response);

        Assert.assertEquals("{\"response\":\"" + RESPONSE + "\"}", output.toString());
    }

    @Test
    public void testDoPostIOException() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        InstantAccessServlet servlet = new InstantAccessServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                                 "\"operation\": \"GetUserId\"," +
                                 "\"infoField1\": \"nobody@amazon.com\"," +
                                 "\"infoField2\": \"amazon\"" +
                             "}";
        // @formatter:on

        mockRequest(request, bodyContent, credential);

        new ByteArrayOutputStream();
        EasyMock.expect(response.getOutputStream()).andReturn(new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("");
            }
        });

        // Response first set to OK and then should be finally set to ISE after exception
        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        EasyMock.replay(request, response);

        servlet.doPost(request, response);

        EasyMock.verify(request, response);
    }

    @Test
    public void testDoPostSerializationException() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        InstantAccessServlet servlet = new InstantAccessServletImpl(store);

        // @formatter:off
        String bodyContent = // missing opening bracket
                                 "\"operation\": \"GetUserId\"," +
                                 "\"infoField1\": \"nobody@amazon.com\"," +
                                 "\"infoField2\": \"amazon\"" +
                             "}";
        // @formatter:on

        mockRequest(request, bodyContent, credential);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        EasyMock.replay(request, response);

        servlet.doPost(request, response);

        EasyMock.verify(request, response);
    }

    @Test
    public void testDoPostEmptyCredentialStore() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        Credential credential = new Credential("SECRETKEY", "KEYID");

        InstantAccessServlet servlet = new InstantAccessServletImpl(new CredentialStore());

        // @formatter:off
        String bodyContent = "{}"; 
        // @formatter:on

        mockRequest(request, bodyContent, credential);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        EasyMock.replay(request, response);

        servlet.doPost(request, response);

        EasyMock.verify(request, response);
    }

    protected void mockRequest(HttpServletRequest request, final String bodyContent, Credential credential)
            throws IOException, SigningException {
        Signer signer = new Signer();
        final Request req = new Request("https://amazon.com:8443/", Method.POST, "application/json");
        req.setBody(bodyContent);

        signer.sign(req, credential);

        EasyMock.expect(request.getRequestURL()).andReturn(new StringBuffer("https://amazon.com:8443/"));
        EasyMock.expect(request.getQueryString()).andReturn(null);
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.expect(request.getContentType()).andReturn("application/json");
        EasyMock.expect(request.getInputStream()).andReturn(new ServletInputStream() {
            private final ByteArrayInputStream stream = new ByteArrayInputStream(bodyContent.getBytes());

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        });

        EasyMock.expect(request.getHeaderNames()).andReturn((new Vector<String>() {
            {
                for (String n : req.getHeaderNames()) {
                    add(n);
                }
            }
        }).elements());

        for (String n : req.getHeaderNames()) {
            EasyMock.expect(request.getHeader(n)).andReturn(req.getHeader(n)).anyTimes();
        }
    }
}

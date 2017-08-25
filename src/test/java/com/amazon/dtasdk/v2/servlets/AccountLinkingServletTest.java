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
package com.amazon.dtasdk.v2.servlets;

import com.amazon.dtasdk.signature.Credential;
import com.amazon.dtasdk.signature.CredentialStore;
import com.amazon.dtasdk.signature.SigningException;
import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableRequest;
import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableResponse;
import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableResponseValue;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for InstantAccessServlet.
 */
@Deprecated
public class AccountLinkingServletTest extends InstantAccessServletTest {

    private static final GetUserIdSerializableResponseValue GETUSERID_RESPONSE = GetUserIdSerializableResponseValue.FAIL_ACCOUNT_INVALID;

    private class AccountLinkingServletImpl extends AccountLinkingServlet {

        private final CredentialStore store;

        public AccountLinkingServletImpl(CredentialStore store) {
            this.store = store;
        }

        @Override
        public CredentialStore getCredentialStore() {
            return store;
        }

        @Override
        public GetUserIdSerializableResponse getUserId(GetUserIdSerializableRequest request) {
            GetUserIdSerializableResponse response = new GetUserIdSerializableResponse();
            response.setResponse(GETUSERID_RESPONSE);
            response.setUserId("1234");

            return response;
        }
    }

    @Test
    public void testGetUserId() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        AccountLinkingServletImpl servlet = new AccountLinkingServletImpl(store);

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

        Assert.assertEquals("{\"response\":\"" + GETUSERID_RESPONSE + "\",\"userId\":\"1234\"}", output.toString());
    }
}

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

import com.amazon.dtasdk.base.SubscriptionResponse;
import com.amazon.dtasdk.base.SubscriptionResponseValue;
import com.amazon.dtasdk.v2.serialization.messages.*;
import com.amazon.dtasdk.signature.Credential;
import com.amazon.dtasdk.signature.CredentialStore;
import com.amazon.dtasdk.signature.SigningException;
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
public class PurchaseServletTest extends InstantAccessServletTest {

    private static final FulfillPurchaseResponseValue FULFILL_RESPONSE = FulfillPurchaseResponseValue.FAIL_USER_INVALID;
    private static final RevokePurchaseResponseValue REVOKE_RESPONSE = RevokePurchaseResponseValue.FAIL_INVALID_PURCHASE_TOKEN;
    private static final SubscriptionResponseValue SUBSCRIPTIONACTION_RESPONSE = SubscriptionResponseValue.FAIL_INVALID_SUBSCRIPTION;

    private class PurchaseServletImpl extends PurchaseServlet {

        private final CredentialStore store;

        public PurchaseServletImpl(CredentialStore store) {
            this.store = store;
        }

        @Override
        public CredentialStore getCredentialStore() {
            return store;
        }

        @Override
        public FulfillPurchaseResponse fulfillPurchase(FulfillPurchaseRequest request) {
            FulfillPurchaseResponse response = new FulfillPurchaseResponse();
            response.setResponse(FULFILL_RESPONSE);

            return response;
        }

        @Override
        public RevokePurchaseResponse revokePurchase(RevokePurchaseRequest request) {
            RevokePurchaseResponse response = new RevokePurchaseResponse();
            response.setResponse(REVOKE_RESPONSE);

            return response;
        }

        @Override
        public SubscriptionResponse processSubscriptionActivate(final SubscriptionActivateRequest request) {
            SubscriptionResponse response = new SubscriptionResponse();
            response.setResponse(SUBSCRIPTIONACTION_RESPONSE);

            return response;
        }

        @Override
        public SubscriptionResponse processSubscriptionDeactivate(final SubscriptionDeactivateRequest request) {
            SubscriptionResponse response = new SubscriptionResponse();
            response.setResponse(SUBSCRIPTIONACTION_RESPONSE);

            return response;
        }
    }

    @Test
    public void testFulfillPurchase() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        PurchaseServletImpl servlet = new PurchaseServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                                 "\"operation\": \"Purchase\"," +
                                 "\"reason\": \"FULFILL\"," +
                                 "\"productId\": \"GamePack1\"," +
                                 "\"userId\": \"123456\"," +
                                 "\"purchaseToken\": \"6f3092e5-0326-42b7-a107-416234d548d8\"" +
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

        Assert.assertEquals("{\"response\":\"" + FULFILL_RESPONSE + "\"}", output.toString());
    }

    @Test
    public void testRevokePurchase() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        PurchaseServletImpl servlet = new PurchaseServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                                 "\"operation\": \"Revoke\"," +
                                 "\"reason\": \"CUSTOMER_SERVICE_REQUEST\"," +
                                 "\"productId\": \"GamePack1\"," +
                                 "\"userId\": \"123456\"," +
                                 "\"purchaseToken\": \"6f3092e5-0326-42b7-a107-416234d548d8\"" +
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

        Assert.assertEquals("{\"response\":\"" + REVOKE_RESPONSE + "\"}", output.toString());
    }

    @Test
    public void testSubscriptionActivate() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        PurchaseServletImpl servlet = new PurchaseServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                                 "\"operation\": \"SubscriptionActivate\"," +
                                 "\"subscriptionId\": \"subscriptionId\"," +
                                 "\"productId\": \"GamePack1\"," +
                                 "\"userId\": \"1234\"" +
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

        Assert.assertEquals("{\"response\":\"" + SUBSCRIPTIONACTION_RESPONSE + "\"}", output.toString());
    }

    @Test
    public void testSubscriptionDeactivate() throws SigningException, IOException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);

        CredentialStore store = new CredentialStore();
        Credential credential = new Credential("SECRETKEY", "KEYID");
        store.add(credential);

        PurchaseServletImpl servlet = new PurchaseServletImpl(store);

        // @formatter:off
        String bodyContent = "{" +
                             "\"operation\": \"SubscriptionDeactivate\"," +
                             "\"subscriptionId\": \"subscriptionId\"," +
                             "\"reason\": \"USER_REQUEST\"," +
                             "\"period\": \"REGULAR\"" +
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

        Assert.assertEquals("{\"response\":\"" + SUBSCRIPTIONACTION_RESPONSE + "\"}", output.toString());
    }
}

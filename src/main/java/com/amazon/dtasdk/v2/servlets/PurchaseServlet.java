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

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.InstantAccessResponse;
import com.amazon.dtasdk.base.SubscriptionResponse;
import com.amazon.dtasdk.v2.serialization.messages.FulfillPurchaseRequest;
import com.amazon.dtasdk.v2.serialization.messages.FulfillPurchaseResponse;
import com.amazon.dtasdk.v2.serialization.messages.RevokePurchaseRequest;
import com.amazon.dtasdk.v2.serialization.messages.RevokePurchaseResponse;
import com.amazon.dtasdk.v2.serialization.messages.SubscriptionActivateRequest;
import com.amazon.dtasdk.v2.serialization.messages.SubscriptionDeactivateRequest;
import com.amazon.dtasdk.serializer.SerializationException;

/**
 * <p>
 * This abstract servlet can be extended to implement the V2 Purchase/Subscription section of Instant Access API.
 * </p>
 *
 * <p>
 * When extending this servlet, you must implement the abstract methods relative to the four operations present in the
 * API. {@link #fulfillPurchase(FulfillPurchaseRequest)} is the method responsible for fulfilling a purchase, {@link
 * #revokePurchase(RevokePurchaseRequest)} is the method responsible for revoking a purchase, {@link
 * #processSubscriptionActivate(SubscriptionActivateRequest)} is the method responsible for activating a subscription and
 * {@link #processSubscriptionDeactivate(SubscriptionDeactivateRequest)} is the method responsible for deactivating a
 * subscription
 * </p>
 *
 * <p>
 * You must also implement the abstract method {@link #getCredentialStore()}. This method must provide a valid credential
 * store that will be used to verify the message authenticity.
 * </p>
 *
 */
public abstract class PurchaseServlet extends InstantAccessServlet {

    /**
     * Process the fulfill purchase request and return the response to whether or not the request succeeded.
     *
     * @param request
     *            the request relative to the fulfill purchase
     *
     * @return a FulfillPurchaseResponse object
     */
    public abstract FulfillPurchaseResponse fulfillPurchase(FulfillPurchaseRequest request);

    /**
     * Process the revoke purchase request and return the response to whether or not the request succeeded.
     *
     * @param request
     *            the request relative to the revoke purchase
     *
     * @return a RevokePurchaseResponse object
     */
    public abstract RevokePurchaseResponse revokePurchase(RevokePurchaseRequest request);

    /**
     * Process a subscription activation based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request
     *            the request object with information about the subscription
     *
     * @return a SubscriptionResponse object
     */
    public abstract SubscriptionResponse processSubscriptionActivate(SubscriptionActivateRequest request);

    /**
     * Process a subscription deactivation based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request
     *            the request object with information about the subscription
     *
     * @return a SubscriptionResponse object
     */
    public abstract SubscriptionResponse processSubscriptionDeactivate(SubscriptionDeactivateRequest request);

    @Override
    public InstantAccessResponse<?> processOperation(InstantAccessOperationValue operation, String requestBody)
            throws SerializationException {

        InstantAccessResponse<?> iaResponse;

        // process the request according to the operation
        switch (operation) {
            case PURCHASE:
                iaResponse = fulfillPurchase(serializer.decode(requestBody, FulfillPurchaseRequest.class));
            break;
            case REVOKE:
                iaResponse = revokePurchase(serializer.decode(requestBody, RevokePurchaseRequest.class));
            break;
            case SUBSCRIPTIONACTIVATE:
                iaResponse = processSubscriptionActivate(serializer.decode(requestBody,
                        SubscriptionActivateRequest.class));
            break;
            case SUBSCRIPTIONDEACTIVATE:
                iaResponse = processSubscriptionDeactivate(serializer.decode(requestBody,
                        SubscriptionDeactivateRequest.class));
            break;
            default:
                throw new IllegalArgumentException(String.format("Operation[%s] not supported by %s", operation.name(),
                        this.getClass().getName()));
        }

        return iaResponse;
    }
}

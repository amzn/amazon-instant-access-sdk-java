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
package com.amazon.dtasdk.v3.servlets;

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.InstantAccessResponse;
import com.amazon.dtasdk.base.SubscriptionResponse;
import com.amazon.dtasdk.serializer.SerializationException;
import com.amazon.dtasdk.v3.serialization.messages.*;

/**
 * <p>
 * This abstract servlet can be extended to implement the V3 Purchase/Subscription section of Instant Access API.
 * </p>
 * <p>
 * <p>
 * When extending this servlet, you must implement the abstract methods relative to the four operations present in the
 * API. {@link #fulfillPurchase( FulfillPurchaseRequest )} is the method responsible for fulfilling a purchase, {@link
 * #revokePurchase( RevokePurchaseRequest )} is the method responsible for revoking a purchase, {@link
 * #processSubscriptionActivate( SubscriptionActivateRequest )} is the method responsible for activating a subscription,
 * {@link #processSubscriptionDeactivate( SubscriptionDeactivateRequest )} is the method responsible for deactivating a
 * subscription, {@link #processSubscriptionUpdate( SubscriptionUpdateRequest )} is the method responsible for updating a
 * subscription and {@link #processSubscriptionGet( SubscriptionGetRequest )} is the method responsible for getting a
 * subscription
 * </p>
 * <p>
 * <p>
 * You must also implement the abstract method {@link #getCredentialStore()}. This method must provide a valid credential
 * store that will be used to verify the message authenticity.
 * </p>
 */
public abstract class PurchaseServlet extends InstantAccessServlet {

    /**
     * Process the fulfill purchase request and return the response to whether or not the request succeeded.
     *
     * @param request the request relative to the fulfill purchase
     * @return a FulfillPurchaseResponse object
     */
    public abstract FulfillPurchaseResponse fulfillPurchase(FulfillPurchaseRequest request);

    /**
     * Process the revoke purchase request and return the response to whether or not the request succeeded.
     *
     * @param request the request relative to the revoke purchase
     * @return a RevokePurchaseResponse object
     */
    public abstract RevokePurchaseResponse revokePurchase(RevokePurchaseRequest request);

    /**
     * Process a subscription activation based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request the request object with information about the subscription
     * @return a SubscriptionResponse object
     */
    public abstract SubscriptionResponse processSubscriptionActivate(SubscriptionActivateRequest request);

    /**
     * Process a subscription deactivation based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request the request object with information about the subscription
     * @return a SubscriptionResponse object
     */
    public abstract SubscriptionResponse processSubscriptionDeactivate(SubscriptionDeactivateRequest request);


    /**
     * Process a subscription get based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request the request object with information about the subscription
     * @return a SubscriptionGetResponse object
     */
    public abstract SubscriptionGetResponse processSubscriptionGet(SubscriptionGetRequest request);

    /**
     * Process a subscription update based on the request and returns the response to whether or not the request
     * succeeded.
     *
     * @param request the request object with information about the subscription
     * @return a SubscriptionResponse object
     */
    public abstract SubscriptionResponse processSubscriptionUpdate(SubscriptionUpdateRequest request);

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
            case SUBSCRIPTIONGET:
                iaResponse = processSubscriptionGet(serializer.decode(requestBody, SubscriptionGetRequest.class));
                break;
            case SUBSCRIPTIONACTIVATE:
                iaResponse = processSubscriptionActivate(serializer.decode(requestBody,
                        SubscriptionActivateRequest.class));
                break;
            case SUBSCRIPTIONDEACTIVATE:
                iaResponse = processSubscriptionDeactivate(serializer.decode(requestBody,
                        SubscriptionDeactivateRequest.class));
                break;
            case SUBSCRIPTIONUPDATE:
                iaResponse = processSubscriptionUpdate(serializer.decode(requestBody, SubscriptionUpdateRequest.class));
                break;
            default:
                throw new IllegalArgumentException(String.format("Operation[%s] not supported by v3 %s", operation.name(),
                        this.getClass().getName()));
        }

        return iaResponse;
    }
}

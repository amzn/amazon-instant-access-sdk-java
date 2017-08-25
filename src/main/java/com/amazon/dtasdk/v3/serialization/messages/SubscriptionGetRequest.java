package com.amazon.dtasdk.v3.serialization.messages;

import com.amazon.dtasdk.base.SubscriptionRequest;

public class SubscriptionGetRequest extends SubscriptionRequest {
    public SubscriptionGetRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public SubscriptionGetRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}

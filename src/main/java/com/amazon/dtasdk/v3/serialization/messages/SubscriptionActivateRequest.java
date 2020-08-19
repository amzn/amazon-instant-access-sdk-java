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
package com.amazon.dtasdk.v3.serialization.messages;

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.SubscriptionRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A request to activate a subscription for a specified user and product
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SubscriptionActivateRequest extends SubscriptionRequest {

    private Integer numberOfLicenses;

    @Override
    public SubscriptionActivateRequest setOperation(InstantAccessOperationValue operationValue) {
        this.operation = operationValue;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public SubscriptionActivateRequest setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public Integer getNumberOfLicenses() {
        return numberOfLicenses;
    }

    public SubscriptionActivateRequest setNumberOfLicenses(Integer numberOfLicenses) {
        this.numberOfLicenses = numberOfLicenses;
        return this;
    }

    public SubscriptionActivateRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public SubscriptionActivateRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionActivateRequest)) return false;
        if (!super.equals(o)) return false;

        final SubscriptionActivateRequest that = (SubscriptionActivateRequest) o;

        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (numberOfLicenses != null ? !numberOfLicenses.equals(that.numberOfLicenses) : that.numberOfLicenses != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (numberOfLicenses != null ? numberOfLicenses.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("operation", operation)
                .append("subscriptionId", subscriptionId)
                .append("productId", productId)
                .append("userId", userId)
                .append("numberOfLicenses", numberOfLicenses)
                .toString();
    }
}

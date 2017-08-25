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
package com.amazon.dtasdk.v2.serialization.messages;

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.InstantAccessRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Serializable FulfillPurchaseRequest object to pass to developer used to fulfill a purchase
 */

public class FulfillPurchaseRequest extends InstantAccessRequest {
    private String purchaseToken;
    private String userId;
    private String productId;
    private String reason;

    @Override
    public FulfillPurchaseRequest setOperation(InstantAccessOperationValue operation) {
        this.operation = operation;
        return this;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public FulfillPurchaseRequest setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public FulfillPurchaseRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public FulfillPurchaseRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public FulfillPurchaseRequest setReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((purchaseToken == null) ? 0 : purchaseToken.hashCode());
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FulfillPurchaseRequest other = (FulfillPurchaseRequest) obj;
        if (purchaseToken == null) {
            if (other.purchaseToken != null) {
                return false;
            }
        } else if (!purchaseToken.equals(other.purchaseToken)) {
            return false;
        }

        if (operation == null) {
            if (other.operation != null) {
                return false;
            }
        } else if (!operation.equals(other.operation)) {
            return false;
        }

        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }

        if (productId == null) {
            if (other.productId != null) {
                return false;
            }
        } else if (!productId.equals(other.productId)) {
            return false;
        }

        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("operation", operation)
                .append("reason", reason).append("productId", productId).append("userId", userId)
                .append("purchaseToken", purchaseToken).toString();
    }
}

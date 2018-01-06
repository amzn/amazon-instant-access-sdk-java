package com.amazon.dtasdk.v3.serialization.messages;

import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.SubscriptionRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SubscriptionUpdateRequest extends SubscriptionRequest {

    private Integer numberOfLicenses;
    private Boolean removeAllUnassignedLicenses;

    @Override
    public SubscriptionUpdateRequest setOperation(InstantAccessOperationValue operationValue) {
        this.operation = operationValue;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public SubscriptionUpdateRequest setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public Integer getNumberOfLicenses() {
        return numberOfLicenses;
    }

    public SubscriptionUpdateRequest setNumberOfLicenses(Integer numberOfLicenses) {
        this.numberOfLicenses = numberOfLicenses;
        return this;
    }

    public Boolean getRemoveAllUnassignedLicenses() {
        return removeAllUnassignedLicenses;
    }

    public SubscriptionUpdateRequest setRemoveAllUnassignedLicenses(Boolean removeAllUnassignedLicenses) {
        this.removeAllUnassignedLicenses = removeAllUnassignedLicenses;
        return this;
    }

    public SubscriptionUpdateRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public SubscriptionUpdateRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionUpdateRequest)) return false;
        if (!super.equals(o)) return false;

        final SubscriptionUpdateRequest that = (SubscriptionUpdateRequest) o;

        if (productId != null ? !productId.equals(that.productId) : that.productId != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;
        if (numberOfLicenses != null ? !numberOfLicenses.equals(that.numberOfLicenses) : that.numberOfLicenses != null)
            return false;
        if (removeAllUnassignedLicenses != null ?
                !removeAllUnassignedLicenses.equals(that.removeAllUnassignedLicenses) :
                that.removeAllUnassignedLicenses != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (numberOfLicenses != null ? numberOfLicenses.hashCode() : 0);
        result = 31 * result + (removeAllUnassignedLicenses != null ? removeAllUnassignedLicenses.hashCode() : 0);
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
                .append("removeAllUnassignedLicenses", removeAllUnassignedLicenses)
                .toString();
    }
}

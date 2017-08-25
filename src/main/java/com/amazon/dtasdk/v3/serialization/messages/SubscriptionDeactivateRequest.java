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
import com.amazon.dtasdk.base.SubscriptionPeriodValue;
import com.amazon.dtasdk.base.SubscriptionReasonValue;
import com.amazon.dtasdk.base.SubscriptionRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A request to deactivate a subscription.
 * Includes the reason for deactivation and the period in which it was deactivated.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SubscriptionDeactivateRequest extends SubscriptionRequest {

    protected SubscriptionReasonValue reason;
    protected SubscriptionPeriodValue period;

    @Override
    public SubscriptionDeactivateRequest setOperation(InstantAccessOperationValue operationValue) {
        this.operation = operationValue;
        return this;
    }

    public SubscriptionReasonValue getReason() {
        return reason;
    }

    public SubscriptionDeactivateRequest setReason(SubscriptionReasonValue reason) {
        this.reason = reason;
        return this;
    }

    public SubscriptionPeriodValue getPeriod() {
        return period;
    }

    public SubscriptionDeactivateRequest setPeriod(SubscriptionPeriodValue period) {
        this.period = period;
        return this;
    }

    @Override
    public SubscriptionDeactivateRequest setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final SubscriptionDeactivateRequest that = (SubscriptionDeactivateRequest) o;

        if (period != that.period) return false;
        if (reason != that.reason) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("operation", operation)
                .append("subscriptionId", subscriptionId)
                .append("reason", reason)
                .append("period", period)
                .toString();
    }
}

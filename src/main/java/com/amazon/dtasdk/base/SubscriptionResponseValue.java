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
package com.amazon.dtasdk.base;

import org.codehaus.jackson.annotate.JsonCreator;

/**
 * Possible response values for inclusion in a SubscriptionResponse
 */
public enum SubscriptionResponseValue {
    OK,
    FAIL_USER_NOT_ELIGIBLE,
    FAIL_USER_INVALID,
    FAIL_INVALID_SUBSCRIPTION,
    FAIL_OTHER;

    @JsonCreator
    public static SubscriptionResponseValue fromJson(String text) {
        // ignore the case when deserializing
        return valueOf(text.toUpperCase());
    }
}

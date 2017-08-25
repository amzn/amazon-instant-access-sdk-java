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

import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableRequest;
import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableResponse;
import com.amazon.dtasdk.base.InstantAccessOperationValue;
import com.amazon.dtasdk.base.InstantAccessResponse;
import com.amazon.dtasdk.serializer.SerializationException;

/**
 * <p>
 * This abstract servlet can be extended to implement the V2 Account Linking section of Instant Access API.
 * </p>
 *
 * <p>
 * When extending this servlet, you must implement the abstract method {@link #getUserId(GetUserIdSerializableRequest)},
 * it is the method for returning a user id based on the parameters in order to link accounts.
 * </p>
 *
 * <p>
 * You must also implement the abstract method {@link #getCredentialStore()}. This method must provide a valid credential
 * store that will be used to verify the message authenticity.
 * </p>
 *
 */
public abstract class AccountLinkingServlet extends InstantAccessServlet {

    /**
     * Process the request and returns the user id
     *
     * @param request
     *            the request relative to the get user id operation
     *
     * @return a GetUserIdSerializableResponse object
     */
    public abstract GetUserIdSerializableResponse getUserId(GetUserIdSerializableRequest request);

    @Override
    public InstantAccessResponse<?> processOperation(InstantAccessOperationValue operation, String requestBody)
            throws SerializationException {

        GetUserIdSerializableResponse iaResponse;

        // process the request according to the operation
        switch (operation) {
            case GETUSERID:
                iaResponse = getUserId(serializer.decode(requestBody, GetUserIdSerializableRequest.class));
            break;
            default:
                throw new IllegalArgumentException(String.format("Operation[%s] not supported by %s", operation.name(),
                        this.getClass().getName()));
        }

        return iaResponse;
    }
}

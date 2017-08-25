/*
 * Copyright 2016-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package examples.servlet;

import com.amazon.dtasdk.signature.CredentialStore;
import com.amazon.dtasdk.v2.serialization.messages.GetUserIdSerializableResponseValue;
import com.amazon.dtasdk.v3.serialization.messages.GetUserIdSerializableRequest;
import com.amazon.dtasdk.v3.serialization.messages.GetUserIdSerializableResponse;
import com.amazon.dtasdk.v3.servlets.AccountLinkingServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet class that listens to the account linking requests
 */
public class AccountLinkingServletImpl extends AccountLinkingServlet {

    private static final Log log = LogFactory.getLog(AccountLinkingServletImpl.class);

    @Override
    public CredentialStore getCredentialStore() {
        CredentialStore store = new CredentialStore();

        /**
         *  Add your public and private keys to the CredentialStore
         */

        return store;
    }

    @Override
    public GetUserIdSerializableResponse getUserId(GetUserIdSerializableRequest request) {
        log.info(String.format("Started getUserId with request[%s]", request));

        GetUserIdSerializableResponse response = new GetUserIdSerializableResponse();

        try {
            /**
             * Retrieve userId and set it in the response object
             * response.setUserId(service.getUserId(request));
             */

            response.setResponse(GetUserIdSerializableResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing getUserId[%s]", request), e);
            response.setResponse(GetUserIdSerializableResponseValue.FAIL_ACCOUNT_INVALID);
        }

        log.info(String.format("Finished getUserId with response[%s]", response));

        return response;
    }
}

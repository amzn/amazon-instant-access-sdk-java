# Amazon-instant-access-sdk-java


## Installation Guide
1. Download the zip file on GitHub:
    ```bash
    git clone https://github.com/amzn/amazon-instant-access-sdk-java.git
    ```
2. Import the SDK project as a Maven project in your IDE.
3. Implement the servlets required for subscriptions or one time purchase.
4. Run Junit tests to ensure everything is working correctly.

#Example Implementation of AccountLinkingServlet
The following example is available under the examples.servlet package:
```java
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


```

## Example Implementation of PurchaseServlet
The following example is available under examples.servlet package:
```java
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

import com.amazon.dtasdk.base.SubscriptionResponse;
import com.amazon.dtasdk.base.SubscriptionResponseValue;
import com.amazon.dtasdk.signature.CredentialStore;
import com.amazon.dtasdk.v2.serialization.messages.FulfillPurchaseResponseValue;
import com.amazon.dtasdk.v2.serialization.messages.RevokePurchaseResponseValue;
import com.amazon.dtasdk.v3.serialization.messages.FulfillPurchaseRequest;
import com.amazon.dtasdk.v3.serialization.messages.FulfillPurchaseResponse;
import com.amazon.dtasdk.v3.serialization.messages.RevokePurchaseRequest;
import com.amazon.dtasdk.v3.serialization.messages.RevokePurchaseResponse;
import com.amazon.dtasdk.v3.serialization.messages.SubscriptionActivateRequest;
import com.amazon.dtasdk.v3.serialization.messages.SubscriptionDeactivateRequest;
import com.amazon.dtasdk.v3.serialization.messages.SubscriptionGetRequest;
import com.amazon.dtasdk.v3.serialization.messages.SubscriptionGetResponse;
import com.amazon.dtasdk.v3.serialization.messages.SubscriptionUpdateRequest;
import com.amazon.dtasdk.v3.servlets.PurchaseServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Servlet class that listens to the purchases/subscriptions requests
 */
public class PurchaseServletImpl extends PurchaseServlet {
    private static final Log log = LogFactory.getLog(PurchaseServletImpl.class);

    @Override
    public CredentialStore getCredentialStore() {
        CredentialStore store = new CredentialStore();

        /**
         *  Add your public and private keys to the CredentialStore
         */

        return store;
    }

    @Override
    public FulfillPurchaseResponse fulfillPurchase(FulfillPurchaseRequest request) {
        log.info(String.format("Started fulfillPurchase with request[%s]", request));

        FulfillPurchaseResponse response = new FulfillPurchaseResponse();

        try {
            /**
             * Add logic to fulfill a purchase
             */
            response.setResponse(FulfillPurchaseResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing team fulfillPurchase[%s]", request), e);
            response.setResponse(FulfillPurchaseResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished fulfillPurchase with response[%s]", response));

        return response;
    }

    @Override
    public RevokePurchaseResponse revokePurchase(RevokePurchaseRequest request) {
        log.info(String.format("Started revokePurchase with request[%s]", request));

        RevokePurchaseResponse response = new RevokePurchaseResponse();

        try {
            /**
             * Add logic to revoke a purchase
             */

            response.setResponse(RevokePurchaseResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing revokePurchase[%s]", request), e);
            response.setResponse(RevokePurchaseResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished revokePurchase with response[%s]", response));

        return response;
    }

    @Override
    public SubscriptionResponse processSubscriptionActivate(SubscriptionActivateRequest request) {
        log.info(String.format("Started processSubscriptionActivate with request[%s]", request));

        SubscriptionResponse response = new SubscriptionResponse();

        try {
            /**
             * Add logic to activate a subscription
             */
            response.setResponse(SubscriptionResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing processSubscriptionActivate[%s]", request), e);
            response.setResponse(SubscriptionResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished processSubscriptionActivate with response[%s]", response));

        return response;
    }

    @Override
    public SubscriptionResponse processSubscriptionDeactivate(SubscriptionDeactivateRequest request) {
        log.info(String.format("Started processSubscriptionDeactivate with request[%s]", request));

        SubscriptionResponse response = new SubscriptionResponse();

        try {
            /**
             * Add logic to deactivate a subscription
             */
            response.setResponse(SubscriptionResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing processSubscriptionDeactivate[%s]", request), e);
            response.setResponse(SubscriptionResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished processSubscriptionDeactivate with response[%s]", response));

        return response;
    }

    @Override
    public SubscriptionGetResponse processSubscriptionGet(SubscriptionGetRequest request) {
        log.info(String.format("Started processSubscriptionGet with request[%s]", request));

        SubscriptionGetResponse response = new SubscriptionGetResponse();

        try {
            /**
             * Add logic to get a subscription
             */
            response.setResponse(SubscriptionResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing processSubscriptionGet[%s]", request), e);
            response.setResponse(SubscriptionResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished processSubscriptionGet with response[%s]", response));

        return response;
    }

    @Override
    public SubscriptionResponse processSubscriptionUpdate(SubscriptionUpdateRequest request) {
        log.info(String.format("Started processSubscriptionUpdate with request[%s]", request));

        SubscriptionResponse response = new SubscriptionResponse();

        try {
            /**
             * Add logic to update a subscription
             */
            response.setResponse(SubscriptionResponseValue.OK);
        } catch (Exception e) {
            log.error(String.format("Error while processing processSubscriptionUpdate[%s]", request), e);
            response.setResponse(SubscriptionResponseValue.FAIL_OTHER);
        }

        log.info(String.format("Finished processSubscriptionUpdate with response[%s]", response));

        return response;
    }
}

```

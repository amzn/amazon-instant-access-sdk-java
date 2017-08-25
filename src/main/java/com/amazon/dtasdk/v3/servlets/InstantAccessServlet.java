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
import com.amazon.dtasdk.base.InstantAccessRequest;
import com.amazon.dtasdk.base.InstantAccessResponse;
import com.amazon.dtasdk.serializer.JacksonSerializer;
import com.amazon.dtasdk.serializer.SerializationException;
import com.amazon.dtasdk.signature.CredentialStore;
import com.amazon.dtasdk.signature.Request;
import com.amazon.dtasdk.signature.Signer;
import com.amazon.dtasdk.signature.SigningException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * This abstract servlet is extended by the {@link PurchaseServlet} and {@link AccountLinkingServlet} in order to
 * implement the Instant Access API. This class should not be used, use the aforementioned classes instead.
 * </p>
 *
 */
public abstract class InstantAccessServlet extends HttpServlet {
    private static final Log log = LogFactory.getLog(InstantAccessServlet.class);
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private final Signer signer = new Signer();

    protected final JacksonSerializer serializer = new JacksonSerializer();

    /**
     * Returns the credential store
     *
     * @return a CredentialStore object with all the credentials
     */
    public abstract CredentialStore getCredentialStore();

    /**
     * Processes the request based on the operation
     *
     * @param operation
     *            The operation being called
     * @param requestBody
     *            The content of the message
     * @return a generic InstantAccessResponse<?> containing the result of the operation
     *
     * @throws SerializationException
     * @throws IllegalArgumentException
     */
    public abstract InstantAccessResponse<?> processOperation(InstantAccessOperationValue operation, String requestBody)
            throws SerializationException;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Request req = new Request(request);

            if (!signer.verify(req, getCredentialStore())) {
                throw new SigningException("Request validation failed.");
            }

            String requestBody = req.getBody();

            // deserialize the content to a InstantAccessRequest object so we can check which operation is going
            // to be called
            InstantAccessRequest iaRequest = serializer.decode(requestBody, InstantAccessRequest.class);

            // process the request according to the operation
            InstantAccessResponse<?> iaResponse = processOperation(iaRequest.getOperation(), requestBody);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().write(serializer.encode(iaResponse).getBytes(CHARSET));
        } catch (IOException e) {
            log.error("Unable to read the v3 request.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (SigningException e) {
            log.error("Unable to verify the v3 request against the credential store.", e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (SerializationException e) {
            log.error("v3 Serialization error.", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unable to process the v3 request.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

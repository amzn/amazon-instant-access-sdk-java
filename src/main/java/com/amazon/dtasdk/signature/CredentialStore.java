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
package com.amazon.dtasdk.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that is used to manage multiple credentials.
 * 
 * The {@link CredentialStore#load()} method can be called to load keys from a {@link File}, a {@link InputStream} or a
 * {@link String}.
 * 
 * Each line of the file/stream/string must contain a secret key and a public key separated by an empty space, for
 * example:
 * 
 * 69b2048d-8bf8-4c1c-b49d-e6114897a9a5 dce53190-1f70-4206-ad28-0e1ab3683161
 * 
 * Credentials, then, can be accessed by the public key using {@link CredentialStore#get(String)}
 * 
 */
public class CredentialStore {
    private static final Log log = LogFactory.getLog(CredentialStore.class);

    private HashMap<String, Credential> store;

    public CredentialStore() {
        store = new HashMap<String, Credential>();
    }

    /**
     * Gets the credential for a given public key.
     * 
     * @param publicKey
     *            the public key
     * @return the credential
     * 
     * @throws CredentialNotFoundException
     */
    public Credential get(String publicKey) throws CredentialNotFoundException {
        if (!store.containsKey(publicKey)) {
            String message = "Credential not found for public key: " + publicKey;
            log.error(message);
            throw new CredentialNotFoundException(message);
        }

        return store.get(publicKey);
    }

    /**
     * Gets the credentials stored in this store.
     * 
     * @return a {@link Collection<Credential>} with all the credentials
     */
    public Collection<Credential> getAll() {
        return store.values();
    }

    /**
     * Adds the new credential to the store. If the store already contains the public key the credential is replaced.
     * 
     * @param credential
     *            the credential object to be added
     */
    public void add(Credential credential) {
        store.put(credential.getPublicKey(), credential);
    }

    /**
     * Removes the credential from the store.
     * 
     * @param publicKey
     *            the public key of the credential to be removed
     */
    public void remove(String publicKey) {
        store.remove(publicKey);
    }

    /**
     * Loads keys from a file and populates the store.
     * 
     * Each line of the file must contain a secret key and a public key separated by an empty space.
     * 
     * @param file
     *            the file object that contains the keys
     * @throws IOException
     */
    public void load(File file) throws IOException {
        if (file == null || !file.exists()) {
            String message = "Invalid keys file object";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        load(new FileInputStream(file));
    }

    /**
     * Loads keys from a input stream and populates the store.
     * 
     * Each line of the file must contain a secret key and a public key separated by an empty space.
     * 
     * @param stream
     *            the stream object that contains the keys
     * @throws IOException
     */
    public void load(InputStream stream) throws IOException {
        if (stream == null) {
            String message = "Invalid keys input stream";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, Charset.defaultCharset());
        String contents = writer.toString();

        load(contents);
    }

    /**
     * Loads keys from a string and populates the store.
     * 
     * Each line of the file must contain a secret key and a public key separated by an empty space.
     * 
     * @param contents
     *            the string object that contains the keys
     * @throws IOException
     */
    public void load(String contents) {
        if (StringUtils.isEmpty(contents)) {
            String message = "Invalid keys";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        String[] lines = contents.split("\\n");

        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i];

            // Ignore blank lines in between credentials
            if (StringUtils.isEmpty(line)) {
                continue;
            }

            // credentials should be separate by an empty space
            String[] keys = line.split("\\s+");

            // Invalid format
            if (keys.length < 2) {
                String message = String.format("Invalid credentials format found on line %d", i);
                log.error(message);
                throw new IllegalArgumentException(message);
            }

            String secretKey = keys[0];
            String publicKey = keys[1];

            store.put(publicKey, new Credential(secretKey, publicKey));
        }
    }
}

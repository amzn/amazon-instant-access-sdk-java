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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CredentialStore test class
 */
public class CredentialStoreTest {

    private static File VALID_FILE;
    private static File INVALID_FILE;

    private static final String[] KEYS = { "69b2048d-8bf8-4c1c-b49d-e6114897a9a5",
            "dce53190-1f70-4206-ad28-0e1ab3683161", "f0a2586d-24ea-432f-a833-2da18f15ebd4",
            "eb3ce251-ef76-48ee-abb0-5886b1a3dfa0", "7568ccc2-9881-4468-ad73-025d16f0662e",
            "5de206ab-3a06-4354-a9a4-bfd6efee8027" };

    private static final String INVALID_KEY = "871dbe31-3b46-4ca5-b9a2-8ad78eac4a4f";

    @BeforeClass
    public static void setUp() throws IOException {
        VALID_FILE = File.createTempFile("store", "csv");
        FileWriter writer = new FileWriter(VALID_FILE);

        writer.write(String.format("%s %s\n", KEYS[0], KEYS[1]));
        // Intentionally check if blank lines are supported
        writer.write(String.format("%s %s\n\n", KEYS[2], KEYS[3]));
        writer.write(String.format("%s %s\n", KEYS[4], KEYS[5]));
        writer.write("\n");

        writer.close();

        INVALID_FILE = File.createTempFile("store", "csv");
        writer = new FileWriter(INVALID_FILE);

        writer.write(String.format("%s%s\n", KEYS[0], KEYS[1]));
        writer.write(String.format("%s %s\n", KEYS[2], KEYS[3]));
        writer.write(String.format("%s %s\n", KEYS[4], KEYS[5]));
        writer.write("\n");

        writer.close();
    }

    @Test
    public void testLoadFromFile() throws IOException, CredentialNotFoundException {
        CredentialStore store = new CredentialStore();
        store.load(VALID_FILE);

        assertCorrectCredentials(store);
    }

    @Test
    public void testLoadFromInputStream() throws IOException, CredentialNotFoundException {
        CredentialStore store = new CredentialStore();
        store.load(new FileInputStream(VALID_FILE));

        assertCorrectCredentials(store);
    }

    @Test
    public void testLoadFromString() throws IOException, CredentialNotFoundException {
        CredentialStore store = new CredentialStore();

        StringWriter writer = new StringWriter();
        IOUtils.copy(new FileInputStream(VALID_FILE), writer, Charset.defaultCharset());
        String contents = writer.toString();

        store.load(contents);

        assertCorrectCredentials(store);
    }

    private void assertCorrectCredentials(CredentialStore store) throws CredentialNotFoundException {
        Assert.assertEquals(KEYS[0], store.get(KEYS[1]).getSecretKey());
        Assert.assertEquals(KEYS[1], store.get(KEYS[1]).getPublicKey());

        Assert.assertEquals(KEYS[2], store.get(KEYS[3]).getSecretKey());
        Assert.assertEquals(KEYS[3], store.get(KEYS[3]).getPublicKey());

        Assert.assertEquals(KEYS[4], store.get(KEYS[5]).getSecretKey());
        Assert.assertEquals(KEYS[5], store.get(KEYS[5]).getPublicKey());
    }

    @Test(expected = CredentialNotFoundException.class)
    public void testGetInvalidCredential() throws IOException, CredentialNotFoundException {
        CredentialStore store = new CredentialStore();
        store.load(new FileInputStream(VALID_FILE));

        store.get(INVALID_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFile() throws IOException, CredentialNotFoundException {
        CredentialStore store = new CredentialStore();
        store.load(INVALID_FILE);
    }
}

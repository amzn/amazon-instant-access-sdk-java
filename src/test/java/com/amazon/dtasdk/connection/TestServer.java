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
package com.amazon.dtasdk.connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Ignore;

/**
 * This is a wrapper for unit tests that brings up a real Jetty HTTP server with a handler.
 */
@Deprecated
@Ignore
public class TestServer {
    private final Server server;
    private final int port;

    public TestServer(AbstractHandler handler) {
        port = 58901 + (int) (Math.random() * 6000);
        server = new Server(port);
        server.setHandler(handler);
    }

    public int getPort() {
        return port;
    }

    public TestServer start() throws Exception {
        server.start();
        return this;
    }

    public void stop() throws Exception {
        server.stop();
        server.join();
    }
}

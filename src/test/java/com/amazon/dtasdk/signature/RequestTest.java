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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

public class RequestTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor_BlankUrl() throws Exception {
        new Request("", Request.Method.GET, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_MalformedURL() throws Exception {
        new Request("blork", Request.Method.GET, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_MalformedURI() throws Exception {
        new Request("http://amazon.com/?s=^IXIC", Request.Method.GET, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_NullUrl() throws Exception {
        new Request(null, Request.Method.GET, "");
    }

    @Test
    public void defaultValues() {
        Request request = new Request("http://amazon.com", Request.Method.GET, "content/type");
        assertEquals("content/type", request.getHeader(Request.CONTENT_TYPE_HEADER));
        assertEquals(new HashSet(Arrays.asList(Request.CONTENT_TYPE_HEADER)), request.getHeaderNames());
        assertEquals(Request.DEFAULT_USER_AGENT, request.getUserAgent());
    }

    @Test
    public void getSetHeader() {
        Request request = new Request("http://amazon.com", Request.Method.GET, "content/type");
        request.setHeader("key", "value");
        assertEquals("value", request.getHeader("key"));
    }

    @Test
    public void getHeaderNames() {
        Request request = new Request("http://amazon.com", Request.Method.GET, "content/type");
        request.setHeader("key", "value");
        request.setHeader("key2", "value");
        assertEquals(new HashSet(Arrays.asList("key", "key2", Request.CONTENT_TYPE_HEADER)), request.getHeaderNames());
    }

    @Test
    public void headersInsenstive() {
        Request request = new Request("http://amazon.com", Request.Method.GET, "content/type");
        request.setHeader("KeyName", "values");

        assertEquals("values", request.getHeader("Keyname"));
        assertEquals("values", request.getHeader("keyName"));
        assertEquals("values", request.getHeader("keyname"));
    }

    @Test
    public void fromHttpServletRequest() throws IOException {
        HttpServletRequest hsr = EasyMock.createMock(HttpServletRequest.class);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("header1", "header1");
        headers.put("header2", "header2");

        EasyMock.expect(hsr.getHeaderNames()).andReturn(Collections.enumeration(headers.keySet()));

        for (Entry<String, String> e : headers.entrySet()) {
            EasyMock.expect(hsr.getHeader(e.getKey())).andReturn(e.getValue());
        }

        EasyMock.expect(hsr.getMethod()).andReturn("GET");
        EasyMock.expect(hsr.getContentType()).andReturn("content-type");
        EasyMock.expect(hsr.getRequestURL()).andReturn(new StringBuffer("http://amazon.com:80/servlet/path"));
        EasyMock.expect(hsr.getQueryString()).andReturn("");

        EasyMock.expect(hsr.getInputStream()).andReturn(new ServletInputStream() {
            private final ByteArrayInputStream stream = new ByteArrayInputStream("body".getBytes());

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        });

        EasyMock.replay(hsr);

        Request request = new Request(hsr);

        assertEquals("header1", request.getHeader("header1"));
        assertEquals("header2", request.getHeader("header2"));
        assertEquals("content-type", request.getHeader(Request.CONTENT_TYPE_HEADER));
        assertEquals(Request.Method.GET, request.getMethod());
        assertEquals("http://amazon.com:80/servlet/path", request.getUrl());
        assertEquals("body", request.getBody());

        EasyMock.verify(hsr);
    }

    @Test
    public void fromHttpServletRequest_withQueryParams() throws IOException {
        HttpServletRequest hsr = EasyMock.createMock(HttpServletRequest.class);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("header1", "header1");
        headers.put("header2", "header2");

        EasyMock.expect(hsr.getHeaderNames()).andReturn(Collections.enumeration(headers.keySet()));

        for (Entry<String, String> e : headers.entrySet()) {
            EasyMock.expect(hsr.getHeader(e.getKey())).andReturn(e.getValue());
        }

        EasyMock.expect(hsr.getMethod()).andReturn("GET");
        EasyMock.expect(hsr.getContentType()).andReturn("content-type");
        EasyMock.expect(hsr.getRequestURL()).andReturn(new StringBuffer("http://amazon.com:8888/servlet"));
        EasyMock.expect(hsr.getQueryString()).andReturn("param=value&param2=value2").anyTimes();

        EasyMock.expect(hsr.getInputStream()).andReturn(new ServletInputStream() {
            private final ByteArrayInputStream stream = new ByteArrayInputStream("body".getBytes());

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        });

        EasyMock.replay(hsr);

        Request request = new Request(hsr);

        assertEquals("header1", request.getHeader("header1"));
        assertEquals("header2", request.getHeader("header2"));
        assertEquals("content-type", request.getHeader(Request.CONTENT_TYPE_HEADER));
        assertEquals(Request.Method.GET, request.getMethod());
        assertEquals("http://amazon.com:8888/servlet?param=value&param2=value2", request.getUrl());
        assertEquals("body", request.getBody());

        EasyMock.verify(hsr);
    }
}

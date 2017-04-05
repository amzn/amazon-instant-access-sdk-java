/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazon.dtasdk.v2.serialization.messages;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.serializer.JacksonSerializer;
import com.amazon.dtasdk.v2.serialization.serializer.SerializationException;
import com.amazon.dtasdk.v2.serialization.messages.InstantAccessRequest;

public class InstantAccessRequestTest {
    private JacksonSerializer serializer = new JacksonSerializer();
    private InstantAccessRequest request = new InstantAccessRequest();
    
    @Test
    public void testSerialize() throws SerializationException {
        GetUserIdSerializableRequest request = new GetUserIdSerializableRequest();
        request.setOperation(InstantAccessOperationValue.GETUSERID);
        request.setInfoField1("nobody@amazon.com");
        request.setInfoField2("AMZN");

        String requestString = serializer.encode(request);

        assertEquals("{\"operation\":\"GetUserId\",\"infoField1\":\"nobody@amazon.com\",\"infoField2\":\"AMZN\"}",
                requestString);
    }

    @Test
    public void testDeserialize() throws SerializationException {
        String str = "{\"operation\" : \"GetUserId\", \"infoField1\" : \"nobody@amazon.com\",\"infoField2\" : \"AMZN\"}";
        GetUserIdSerializableRequest request = serializer.decode(str, GetUserIdSerializableRequest.class);

        assertEquals(InstantAccessOperationValue.GETUSERID, request.getOperation());
        assertEquals("nobody@amazon.com", request.getInfoField1());
        assertEquals("AMZN", request.getInfoField2());
    }

    @Test
    public void testDeserializeExtraFields() throws SerializationException {
        String str = "{\"operation\" : \"GetUserId\", \"infoField1\" : \"nobody@amazon.com\",\"infoField2\" : \"AMZN\", \"newField\":\"newValue\"}";
        GetUserIdSerializableRequest request = serializer.decode(str, GetUserIdSerializableRequest.class);

        assertEquals(InstantAccessOperationValue.GETUSERID, request.getOperation());
        assertEquals("nobody@amazon.com", request.getInfoField1());
        assertEquals("AMZN", request.getInfoField2());
    }
    
    @Test
    public void testHashCode() {
    	int result = request.hashCode();
    	assertNotNull(result);
    	assertEquals(31, result);
    	
        request.setOperation(InstantAccessOperationValue.GETUSERID);
        int result2 = request.hashCode();
    	assertNotNull(result2);
    }
    
    @Test
    public void testEquals() {
    	boolean result = request.equals(request);
    	assertNotNull(result);
    	assertEquals(result, true);
    	
    	boolean result2 = request.equals(null);
    	assertNotNull(result2);
    	assertEquals(result2, false);
    	
    	boolean result3 = request.equals(serializer);
    	assertNotNull(result3);
    	assertEquals(result3, false);
    	
    	InstantAccessRequest other = new InstantAccessRequest();
    	other.setOperation(InstantAccessOperationValue.GETUSERID);
    	//request.setOperation(null);
    	boolean result4 = request.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	InstantAccessRequest other2 = new InstantAccessRequest();
    	boolean result6 = request.equals(other2);
    	assertNotNull(result6);
    	assertEquals(result6, true);
    }
    
    @Test
    public void testToString() {
    	String result = request.toString();
    	assertNotNull(result);
    	assertEquals("InstantAccessRequest[operation=<null>]", result);
    }
    
}

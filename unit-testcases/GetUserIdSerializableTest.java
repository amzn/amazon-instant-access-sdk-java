package com.amazon.dtasdk.v2.serialization.messages;

import static org.junit.Assert.*;
import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.serializer.JacksonSerializer;
import com.amazon.dtasdk.v2.serialization.serializer.SerializationException;
//import com.amazon.dtasdk.v2.serialization.messages.InstantAccessRequest;

public class GetUserIdSerializableTest {
	private JacksonSerializer serializer = new JacksonSerializer();
    private GetUserIdSerializableRequest request = new GetUserIdSerializableRequest();
    private GetUserIdSerializableResponse response = new GetUserIdSerializableResponse();
    
    @Test
    public void testSerialize() throws SerializationException {
        GetUserIdSerializableRequest request = new GetUserIdSerializableRequest();
        request.setOperation(InstantAccessOperationValue.GETUSERID);
        request.setInfoField1("nobody@amazon.com");
        request.setInfoField2("AMZN");
        request.setInfoField3("1234");

        String requestString = serializer.encode(request);

        assertEquals("{\"operation\":\"GetUserId\",\"infoField1\":\"nobody@amazon.com\",\"infoField2\":\"AMZN\",\"infoField3\":\"1234\"}",
                requestString);
    }

    @Test
    public void testDeserialize() throws SerializationException {
        String str = "{\"operation\" : \"GetUserId\", \"infoField1\" : \"nobody@amazon.com\",\"infoField2\" : \"AMZN\",\"infoField3\":\"1234\"}";
        GetUserIdSerializableRequest request = serializer.decode(str, GetUserIdSerializableRequest.class);

        assertEquals(InstantAccessOperationValue.GETUSERID, request.getOperation());
        assertEquals("nobody@amazon.com", request.getInfoField1());
        assertEquals("AMZN", request.getInfoField2());
        assertEquals("1234", request.getInfoField3());
    }
    
    @Test
    public void testHashCode() {
    	int result = request.hashCode();
    	assertNotNull(result);
    	assertEquals(923521, result);
    	
    	request.setOperation(InstantAccessOperationValue.GETUSERID);
        request.setInfoField1("nobody@amazon.com");
        request.setInfoField2("AMZN");
        request.setInfoField3("1234");
        int result2 = request.hashCode();
    	assertNotNull(result2);
    	//assertEquals(48829561, result2);
    }
    
    @Test
    public void testEquals2() {
    	boolean result = response.equals(response);
    	assertNotNull(result);
    	assertEquals(result, true);
    	
    	boolean result2 = response.equals(null);
    	assertNotNull(result2);
    	assertEquals(result2, false);
    	
    	boolean result3 = response.equals(serializer);
    	assertNotNull(result3);
    	assertEquals(result3, false);
    	
    	GetUserIdSerializableResponse other = new GetUserIdSerializableResponse();
    	other.setUserId("1234");
    	boolean result4 = response.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	GetUserIdSerializableResponse other6 = new GetUserIdSerializableResponse();
    	boolean result9 = response.equals(other6);
    	assertNotNull(result9);
    	assertEquals(result9, true);
    }
    
    @Test
    public void testHashCode2() {
    	int result = response.hashCode();
    	assertNotNull(result);
    	assertEquals(961, result);
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
    	
    	GetUserIdSerializableRequest other = new GetUserIdSerializableRequest();
    	other.setOperation(InstantAccessOperationValue.GETUSERID);
    	boolean result4 = request.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	GetUserIdSerializableRequest other2 = new GetUserIdSerializableRequest();
    	other2.setInfoField1("nobody@amazon.com");
    	boolean result5 = request.equals(other2);
    	assertNotNull(result5);
    	assertEquals(result5, false);
    	
    	GetUserIdSerializableRequest other3 = new GetUserIdSerializableRequest();
    	other3.setInfoField2("AMZN");
    	boolean result6 = request.equals(other3);
    	assertNotNull(result6);
    	assertEquals(result6, false);
    	
    	GetUserIdSerializableRequest other4 = new GetUserIdSerializableRequest();
    	other4.setInfoField3("1234");
    	boolean result7 = request.equals(other4);
    	assertNotNull(result7);
    	assertEquals(result7, false);
    	
    	GetUserIdSerializableRequest other6 = new GetUserIdSerializableRequest();
    	boolean result9 = request.equals(other6);
    	assertNotNull(result9);
    	assertEquals(result9, true);
    }

}

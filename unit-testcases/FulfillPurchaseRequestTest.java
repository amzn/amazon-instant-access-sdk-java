package com.amazon.dtasdk.v2.serialization.messages;

import static org.junit.Assert.*;
import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.serializer.JacksonSerializer;
//import com.amazon.dtasdk.v2.serialization.serializer.SerializationException;
import com.amazon.dtasdk.v2.serialization.messages.FulfillPurchaseRequest;

public class FulfillPurchaseRequestTest {
	private JacksonSerializer serializer = new JacksonSerializer();
	private FulfillPurchaseRequest request = new FulfillPurchaseRequest();
	
	@Test
    public void testFulfillPurchaseRequest() {
        //RevokePurchaseRequest request = new RevokePurchaseRequest();
        request.setPurchaseToken("abcd");
        request.setUserId("nobody@amazon.com");
        request.setProductId("1234");
        request.setReason("reason");
        
        assertEquals("abcd", request.getPurchaseToken());
        assertEquals("nobody@amazon.com", request.getUserId());
        assertEquals("1234", request.getProductId());
        assertEquals("reason", request.getReason());
    }
	
	@Test
    public void testHashCode() {
    	int result = request.hashCode();
    	assertNotNull(result);
    	assertEquals(28629151, result);
    	
    	request.setOperation(InstantAccessOperationValue.GETUSERID);
    	request.setPurchaseToken("abcd");
        request.setUserId("nobody@amazon.com");
        request.setProductId("1234");
        request.setReason("reason");
        int result2 = request.hashCode();
    	assertNotNull(result2);
    	//assertEquals(1829464621, result2);
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
    	
    	FulfillPurchaseRequest other = new FulfillPurchaseRequest();
    	other.setOperation(InstantAccessOperationValue.GETUSERID);
    	boolean result4 = request.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	FulfillPurchaseRequest other2 = new FulfillPurchaseRequest();
    	other2.setPurchaseToken("abcd");
    	boolean result5 = request.equals(other2);
    	assertNotNull(result5);
    	assertEquals(result5, false);
    	
    	FulfillPurchaseRequest other3 = new FulfillPurchaseRequest();
    	other3.setUserId("nobody@amazon.com");
    	boolean result6 = request.equals(other3);
    	assertNotNull(result6);
    	assertEquals(result6, false);
    	
    	FulfillPurchaseRequest other4 = new FulfillPurchaseRequest();
    	other4.setProductId("1234");
    	boolean result7 = request.equals(other4);
    	assertNotNull(result7);
    	assertEquals(result7, false);
    	
    	FulfillPurchaseRequest other5 = new FulfillPurchaseRequest();
    	other5.setReason("reason");
    	boolean result8 = request.equals(other5);
    	assertNotNull(result8);
    	assertEquals(result8, false);
    	
    	FulfillPurchaseRequest other6 = new FulfillPurchaseRequest();
    	boolean result9 = request.equals(other6);
    	assertNotNull(result9);
    	assertEquals(result9, true);
    }
	
	@Test
    public void testToString() {
    	String result = request.toString();
    	assertNotNull(result);
    	assertEquals("FulfillPurchaseRequest"
    			+ "[operation=<null>,reason=<null>,productId=<null>,userId=<null>,purchaseToken=<null>]", result);
    }

}

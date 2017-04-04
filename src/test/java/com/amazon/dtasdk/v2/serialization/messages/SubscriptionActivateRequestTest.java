package com.amazon.dtasdk.v2.serialization.messages;

import static org.junit.Assert.*;
import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.serializer.JacksonSerializer;
import com.amazon.dtasdk.v2.serialization.messages.SubscriptionActivateRequest;

public class SubscriptionActivateRequestTest {
	private static final Object Integer = 1;
	private JacksonSerializer serializer = new JacksonSerializer();
	private SubscriptionActivateRequest request = new SubscriptionActivateRequest();
	private SubscriptionRequest request2 = new SubscriptionRequest();
	
	
	@Test
    public void testFulfillPurchaseRequest() {
        request.setProductId("1234");
        request.setUserId("nobody@amazon.com");
        request.setNumberOfSubscriptionsInGroup(1);
        request.setSubscriptionGroupId("Adidas");
        request2.setSubscriptionId("5678");
        
        assertEquals("1234", request.getProductId());
        assertEquals("nobody@amazon.com", request.getUserId());
        assertEquals(Integer, request.getNumberOfSubscriptionsInGroup());
        assertEquals("Adidas", request.getSubscriptionGroupId());
        assertEquals("5678", request2.getSubscriptionId());
    }
	
	@Test
    public void testEquals() {
    	boolean result = request.equals(request);
    	assertNotNull(result);
    	assertEquals(result, true);
    	
    	boolean result10 = request2.equals(request2);
    	assertNotNull(result10);
    	assertEquals(result10, true);
    	
    	boolean result2 = request.equals(null);
    	assertNotNull(result2);
    	assertEquals(result2, false);
    	
    	boolean result11 = request2.equals(null);
    	assertNotNull(result11);
    	assertEquals(result11, false);
    	
    	boolean result3 = request.equals(serializer);
    	assertNotNull(result3);
    	assertEquals(result3, false);
    	
    	boolean result12 = request2.equals(serializer);
    	assertNotNull(result12);
    	assertEquals(result12, false);
    	
    	SubscriptionActivateRequest other = new SubscriptionActivateRequest();
    	other.setOperation(InstantAccessOperationValue.GETUSERID);
    	boolean result4 = request.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	SubscriptionActivateRequest other2 = new SubscriptionActivateRequest();
    	other2.setNumberOfSubscriptionsInGroup(1);
    	boolean result5 = request.equals(other2);
    	assertNotNull(result5);
    	assertEquals(result5, false);
    	
    	SubscriptionActivateRequest other3 = new SubscriptionActivateRequest();
    	other3.setUserId("nobody@amazon.com");
    	boolean result6 = request.equals(other3);
    	assertNotNull(result6);
    	assertEquals(result6, false);
    	
    	SubscriptionActivateRequest other4 = new SubscriptionActivateRequest();
    	other4.setProductId("1234");
    	boolean result7 = request.equals(other4);
    	assertNotNull(result7);
    	assertEquals(result7, false);
    	
    	SubscriptionActivateRequest other5 = new SubscriptionActivateRequest();
    	other5.setSubscriptionGroupId("Adidas");
    	boolean result8 = request.equals(other5);
    	assertNotNull(result8);
    	assertEquals(result8, false);
    	
    	SubscriptionActivateRequest other6 = new SubscriptionActivateRequest();
    	boolean result9 = request.equals(other6);
    	assertNotNull(result9);
    	assertEquals(result9, true);
    	
    	SubscriptionRequest other7 = new SubscriptionRequest();
    	boolean result13 = request2.equals(other7);
    	assertNotNull(result13);
    	assertEquals(result13, true);
    	
    	SubscriptionRequest other8 = new SubscriptionRequest();
    	other8.setSubscriptionId("5678");
    	boolean result14 = request2.equals(other8);
    	assertNotNull(result14);
    	assertEquals(result14, false);
    }
	
	@Test
    public void testHashCode() {
    	int result = request.hashCode();
    	assertNotNull(result);
    	assertEquals(887503681, result);
    	
    	request.setOperation(InstantAccessOperationValue.GETUSERID);
    	request.setProductId("1234");
        request.setUserId("nobody@amazon.com");
        request.setNumberOfSubscriptionsInGroup(1);
        request.setSubscriptionGroupId("Adidas");
        int result2 = request.hashCode();
    	assertNotNull(result2);
    	//assertEquals(1829464621, result2);
    	
    	int result3 = request2.hashCode();
    	assertNotNull(result3);
    	assertEquals(961, result3);
    	
    	request2.setSubscriptionId("5678");
    	int result4 = request2.hashCode();
    	assertNotNull(result4);
    }
	
	@Test
    public void testToString() {
    	String result = request.toString();
    	assertNotNull(result);
    	assertEquals("SubscriptionActivateRequest"
    			+ "[operation=<null>,subscriptionId=<null>,productId=<null>,userId=<null>,"
    			+ "numberOfSubscriptionsInGroup=<null>,subscriptionGroupId=<null>]", result);
    }
}

package com.amazon.dtasdk.v2.serialization.messages;

import static org.junit.Assert.*;
import org.junit.Test;

import com.amazon.dtasdk.v2.serialization.serializer.JacksonSerializer;
import com.amazon.dtasdk.v2.serialization.messages.SubscriptionDeactivateRequest;

public class SubscriptionDeactivateRequestTest {
	private JacksonSerializer serializer = new JacksonSerializer();
	private SubscriptionDeactivateRequest request = new SubscriptionDeactivateRequest();
	SubscriptionReasonValue reason;
	SubscriptionPeriodValue period;
	
	@Test
    public void testSubscriptionDeactivateRequest() {
        assertEquals(reason, request.getReason());
        assertEquals(period, request.getPeriod());
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
    	
    	SubscriptionDeactivateRequest other = new SubscriptionDeactivateRequest();
    	other.setOperation(InstantAccessOperationValue.GETUSERID);
    	boolean result4 = request.equals(other);
    	assertNotNull(result4);
    	assertEquals(result4, false);
    	
    	SubscriptionDeactivateRequest other2 = new SubscriptionDeactivateRequest();
    	other2.setReason(reason);
    	boolean result5 = request.equals(other2);
    	assertNotNull(result5);
    	assertEquals(result5, true);
    	
    	SubscriptionDeactivateRequest other3 = new SubscriptionDeactivateRequest();
    	other3.setPeriod(period);
    	boolean result6 = request.equals(other3);
    	assertNotNull(result6);
    	assertEquals(result6, true);
    	
    	
    	SubscriptionDeactivateRequest other6 = new SubscriptionDeactivateRequest();
    	boolean result9 = request.equals(other6);
    	assertNotNull(result9);
    	assertEquals(result9, true);
    }
	
	@Test
    public void testHashCode() {
    	int result = request.hashCode();
    	assertNotNull(result);
    	assertEquals(923521, result);
    	
    	request.setOperation(InstantAccessOperationValue.GETUSERID);
		request.setReason(reason);
		request.setPeriod(period);
        int result2 = request.hashCode();
    	assertNotNull(result2);
    	//assertEquals(1829464621, result2);
    }
	
	@Test
    public void testToString() {
    	String result = request.toString();
    	assertNotNull(result);
    	assertEquals("SubscriptionDeactivateRequest"
    			+ "[operation=<null>,subscriptionId=<null>,reason=<null>,period=<null>]", result);
    }

}

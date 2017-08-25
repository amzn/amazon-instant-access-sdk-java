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
package com.amazon.dtasdk.serializer;

import com.amazon.dtasdk.serializer.TestJson.Name;
import junit.framework.Assert;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class JacksonSerializerTest {
    private static final String jsonString = "{\"value\":\"theValue\","
            + "\"names\":[{\"first\":\"Joe\",\"last\":\"Sixpack\"}," + "{\"first\":\"Bill\",\"last\":\"Fourpack\"}]}";

    private static final JacksonSerializer serializer = new JacksonSerializer();

    @Test
    public void testDecode() throws Exception {
        TestJson dtgObject = serializer.decode(jsonString, TestJson.class);

        Assert.assertEquals(TestJson.class, dtgObject.getClass());
        TestJson testJson = dtgObject;
        Assert.assertEquals("Joe", testJson.getNames().get(0).getFirst());
        Assert.assertEquals("Sixpack", testJson.getNames().get(0).getLast());
        Assert.assertEquals("Bill", testJson.getNames().get(1).getFirst());
        Assert.assertEquals("Fourpack", testJson.getNames().get(1).getLast());
        Assert.assertEquals("theValue", testJson.getValue());
    }

    @Test
    public void testEncode() throws Exception {
        String encodedJson = encodeObject(getTestObject());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree(jsonString);
        JsonNode actual = mapper.readTree(encodedJson);

        Assert.assertEquals(expected, actual);
    }

    private TestJson getTestObject() throws Exception {
        TestJson testJson = new TestJson();
        Name name = new Name();
        name.setFirst("Joe");
        name.setLast("Sixpack");

        Name name2 = new Name();
        name2.setFirst("Bill");
        name2.setLast("Fourpack");

        List<Name> names = new LinkedList<Name>();
        names.add(name);
        names.add(name2);

        testJson.setNames(names);
        testJson.setValue("theValue");

        return testJson;
    }

    private String encodeObject(TestJson testJson) throws SerializationException {
        return serializer.encode(testJson);
    }

    @Test
    public void testEncodeAndDecode() throws Exception {
        TestJson testObject = getTestObject();
        String json = encodeObject(testObject);

        Object decoded = serializer.decode(json, TestJson.class);
        Assert.assertEquals(testObject, decoded);
    }

    @Test(expected = SerializationException.class)
    public void badJsonDecodeTest() throws Exception {
        String badJson = "{fjan";

        serializer.decode(badJson, TestJson.class);
    }

    @Test(expected = SerializationException.class)
    public void testNonExistentClass() throws Exception {
        String badJson = "{\"Value\":\"theValue\","
                + "\"Names\":[{\"First\":\"Joe\",\"Last\":\"Sixpack\"},{\"First\":\"Bill\",\"Last\":\"Fourpack\"}],"
                + "\"Type\":\"NonExistent\"}";

        serializer.decode(badJson, TestJson.class);
    }

    @Test(expected = SerializationException.class)
    public void testJsonDoesntMapToClass() throws Exception {
        String badJson = "{\"Names\":[{\"First\":\"Joe\",\"Last\":\"Sixpack\"},{\"First\":\"Bill\",\"Last\":\"Fourpack\"}],"
                + "\"Type\":\"NonExistent\"}";

        serializer.decode(badJson, TestJson.class);
    }
}

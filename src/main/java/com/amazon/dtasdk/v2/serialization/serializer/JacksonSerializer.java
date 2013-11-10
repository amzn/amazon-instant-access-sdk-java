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
package com.amazon.dtasdk.v2.serialization.serializer;

import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonSerializer {
    private static final Log log = LogFactory.getLog(JacksonSerializer.class);

    private ObjectMapper objectMapper;

    public JacksonSerializer() {
        objectMapper = new ObjectMapper();
    }

    public <T> String encode(T serialObject) throws SerializationException {
        log.info(String.format("Serializing object [%s]", serialObject));

        StringWriter writer = new StringWriter();

        try {
            objectMapper.writeValue(writer, serialObject);
        } catch (Exception e) {
            String error = "Unable to serialize object to string";
            log.error(error, e);
            throw new SerializationException(error, e);
        }

        return writer.toString();
    }

    public <T> T decode(String jsonString, Class<T> pojoClass) throws SerializationException {
        try {
            return objectMapper.readValue(jsonString, pojoClass);
        } catch (Exception e) {
            String error = "Unable to deserialize string into object";
            log.error(error, e);
            throw new SerializationException(error, e);
        }
    }
}
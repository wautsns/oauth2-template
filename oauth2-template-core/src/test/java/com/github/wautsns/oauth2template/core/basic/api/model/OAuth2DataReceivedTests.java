/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wautsns.oauth2template.core.basic.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

/**
 * Tests for {@link OAuth2DataReceived}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2DataReceivedTests {

    @Test
    void getBoolean_String_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        assertNull(instance.getBoolean("name"));
        rawData.put("name", true);
        assertEquals(Boolean.TRUE, instance.getBoolean("name"));
        rawData.put("name", "true");
        assertEquals(Boolean.TRUE, instance.getBoolean("name"));
    }

    @Test
    void getBoolean_String_NullName() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.getBoolean(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getLong_String_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        assertNull(instance.getBoolean("name"));
        rawData.put("name", 123L);
        assertEquals(123L, instance.getLong("name"));
        rawData.put("name", "123");
        assertEquals(123L, instance.getLong("name"));
    }

    @Test
    void getLong_String_NullName() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.getLong(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getString_String_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        assertNull(instance.getBoolean("name"));
        rawData.put("name", "value");
        assertEquals("value", instance.getString("name"));
        rawData.put("name", 123L);
        assertEquals("123", instance.getString("name"));
        rawData.put("name", true);
        assertEquals("true", instance.getString("name"));
    }

    @Test
    void getString_String_NullName() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.getLong(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getValue_String$Function_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        Function<JsonNode, Duration> convertor = node -> Duration.parse(node.asText());
        assertNull(instance.getValue("name", convertor));
        rawData.put("name", "PT3M");
        assertEquals(Duration.parse("PT3M"), instance.getValue("name", convertor));
    }

    @Test
    void getValue_String$Function_NullName() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        Function<JsonNode, Duration> convertor = node -> Duration.parse(node.asText());
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.getValue(null, convertor));
    }

    @Test
    void getValue_String$Function_NullConvertor() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.getValue("name", null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2DataReceived_JsonNode_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData);
        assertSame(rawData, instance.getRawData());
        assertTrue(timestamp <= instance.getTimestamp());
        assertNotNull(instance.getContext());
    }

    @Test
    void OAuth2DataReceived_JsonNode$long_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData, timestamp);
        assertSame(rawData, instance.getRawData());
        assertEquals(timestamp, instance.getTimestamp());
        assertNotNull(instance.getContext());
    }

    @Test
    void OAuth2DataReceived_JsonNode$long$OAuth2Context_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        OAuth2Context context = OAuth2Context.hashMap();
        OAuth2DataReceived instance = new NormalOAuth2DataReceived(rawData, timestamp, context);
        assertSame(rawData, instance.getRawData());
        assertEquals(timestamp, instance.getTimestamp());
        assertSame(context, instance.getContext());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        OAuth2Context context = OAuth2Context.hashMap();
        OAuth2DataReceived instance = spy(new NormalOAuth2DataReceived(rawData, 0, context));
        doReturn(new OAuth2Platform("name")).when(instance).getPlatform();
        assertEquals("[name|0] rawData:{},context:{}", instance.toString());
        rawData.put("name", "value");
        assertEquals("[name|0] rawData:{\"name\":\"value\"},context:{}", instance.toString());
        context.put("name", "value");
        assertEquals(
                "[name|0] rawData:{\"name\":\"value\"},context:{name=value}",
                instance.toString()
        );
    }

    // ##################################################################################

    static class NormalOAuth2DataReceived extends OAuth2DataReceived {

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return mock(OAuth2Platform.class);
        }

        public NormalOAuth2DataReceived(@NotNull JsonNode rawData) {
            super(rawData);
        }

        public NormalOAuth2DataReceived(@NotNull JsonNode rawData, long timestamp) {
            super(rawData, timestamp);
        }

        public NormalOAuth2DataReceived(
                @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
            super(rawData, timestamp, context);
        }

    }

}

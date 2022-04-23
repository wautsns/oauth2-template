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
package com.github.wautsns.oauth2template.core.basic.api.model.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2Callback}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2CallbackTests {

    @Test
    void OAuth2Callback_JsonNode_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        NormalOAuth2Callback instance = new NormalOAuth2Callback(rawData);
        assertSame(rawData, instance.getRawData());
        assertTrue(timestamp <= instance.getTimestamp());
        assertNotNull(instance.getContext());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2Callback_JsonNode$long_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        NormalOAuth2Callback instance = new NormalOAuth2Callback(rawData, timestamp);
        assertSame(rawData, instance.getRawData());
        assertEquals(timestamp, instance.getTimestamp());
        assertNotNull(instance.getContext());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2Callback_JsonNode$long$OAuth2Context_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        long timestamp = System.currentTimeMillis();
        OAuth2Context context = OAuth2Context.hashMap();
        NormalOAuth2Callback instance = new NormalOAuth2Callback(rawData, timestamp, context);
        assertSame(rawData, instance.getRawData());
        assertEquals(timestamp, instance.getTimestamp());
        assertSame(context, instance.getContext());
    }

    // ##################################################################################

    static class NormalOAuth2Callback extends OAuth2Callback {

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return mock(OAuth2Platform.class);
        }

        @Override
        public @Nullable String getCode() {
            return getString("code");
        }

        @Override
        public @Nullable String getState() {
            return getString("state");
        }

        protected NormalOAuth2Callback(@NotNull JsonNode rawData) {
            super(rawData);
        }

        protected NormalOAuth2Callback(@NotNull JsonNode rawData, long timestamp) {
            super(rawData, timestamp);
        }

        protected NormalOAuth2Callback(
                @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
            super(rawData, timestamp, context);
        }

    }

}

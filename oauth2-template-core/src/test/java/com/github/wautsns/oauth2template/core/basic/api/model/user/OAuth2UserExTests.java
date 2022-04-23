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
package com.github.wautsns.oauth2template.core.basic.api.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.api.model.user.properties.OAuth2UserOpenIdSupplier;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Tests for {@link OAuth2UserEx}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2UserExTests {

    @Test
    void getPlatform_NoArg_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        NormalOAuth2User user = new NormalOAuth2User(rawData);
        OAuth2UserEx instance = user.extra();
        assertSame(user.getPlatform(), instance.getPlatform());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getIdentifier_NoArg_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        NormalOAuth2User user = new NormalOAuth2User(rawData);
        OAuth2UserEx instance = user.extra();
        rawData.put("identifier", "value");
        assertEquals(user.getIdentifier(), instance.getIdentifier());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getOpenId_NoArg_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        NormalOAuth2User user = new NormalOAuth2User(rawData);
        OAuth2UserEx instance = user.extra();
        assertNull(instance.getOpenId());
        rawData.put("openId", "value");
        assertEquals(user.getOpenId(), instance.getOpenId());
        assertNull(new SimpleOAuth2User().extra().getOpenId());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getDelegate_NoArg_Normal() {
        ObjectNode rawData = new ObjectMapper().createObjectNode();
        NormalOAuth2User user = new NormalOAuth2User(rawData);
        OAuth2UserEx instance = user.extra();
        assertSame(user, instance.getDelegate());
    }

    // ##################################################################################

    static class SimpleOAuth2User extends OAuth2User {

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return mock(OAuth2Platform.class);
        }

        @Override
        public @NotNull String getIdentifier() {
            return "identifier";
        }

        public SimpleOAuth2User() {
            super(new ObjectMapper().createObjectNode());
        }

    }

    static class NormalOAuth2User extends OAuth2User
            implements OAuth2UserOpenIdSupplier {

        private final OAuth2Platform platform = mock(OAuth2Platform.class);

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return platform;
        }

        @Override
        public @NotNull String getIdentifier() {
            return Objects.requireNonNull(getString("identifier"));
        }

        @Override
        public @UnknownNullability String getOpenId() {
            return getString("openId");
        }

        protected NormalOAuth2User(@NotNull JsonNode rawData) {
            super(rawData);
        }

        protected NormalOAuth2User(@NotNull JsonNode rawData, long timestamp) {
            super(rawData, timestamp);
        }

        protected NormalOAuth2User(
                @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
            super(rawData, timestamp, context);
        }

    }

}

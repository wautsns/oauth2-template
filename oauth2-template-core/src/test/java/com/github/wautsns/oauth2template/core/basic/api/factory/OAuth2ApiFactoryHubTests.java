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
package com.github.wautsns.oauth2template.core.basic.api.factory;

import static com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub.find;
import static com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub.optional;
import static com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.model.callback.OAuth2Callback;
import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.basic.api.model.user.OAuth2User;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2ApiFactoryHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiFactoryHubTests {

    private static final String MOCK_NAME_PREFIX = OAuth2ApiFactoryHubTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "platform#" + name;
    }

    private static void registerMockedOAuth2ApiFactory(String platformName) {
        OAuth2Platform platform = new OAuth2Platform(platformName);
        OAuth2ApiFactory<?, ?, ?, ?> factory = spy(OAuth2ApiFactory.class);
        doReturn(platform).when(factory).getPlatform();
        OAuth2ApiFactoryHub.Manipulation.register(factory);
    }

    // ##################################################################################

    @BeforeAll
    static void beforeAll() {
        assertNotNull(find(mockPlatformName("ServiceProvider")));
    }

    // ##################################################################################

    @Test
    void find_String_Normal() {
        String platformName = mockPlatformName("find_String_Normal");
        OAuth2ApiFactory<?, ?, ?, ?> instanceA = find(platformName);
        assertNull(instanceA);
        registerMockedOAuth2ApiFactory(platformName);
        OAuth2ApiFactory<?, ?, ?, ?> instanceB = find(platformName);
        assertNotNull(instanceB);
        assertSame(platformName, instanceB.getPlatform().getName());
    }

    @Test
    void find_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_String_Normal() {
        String platformName = mockPlatformName("optional_String_Normal");
        Optional<OAuth2ApiFactory<?, ?, ?, ?>> optionalA = optional(platformName);
        assertFalse(optionalA.isPresent());
        registerMockedOAuth2ApiFactory(platformName);
        Optional<OAuth2ApiFactory<?, ?, ?, ?>> optionalB = optional(platformName);
        assertTrue(optionalB.isPresent());
        assertSame(platformName, optionalB.get().getPlatform().getName());
    }

    @Test
    void optional_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_String_Normal() {
        String platformName = mockPlatformName("acquire_String_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        OAuth2ApiFactory<?, ?, ?, ?> instance = acquire(platformName);
        assertNotNull(instance);
        assertEquals(platformName, instance.getPlatform().getName());
    }

    @Test
    void acquire_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null));
    }

    @Test
    void acquire_String_NotExists() {
        String platformName = mockPlatformName("acquire_String_NotExists");
        assertThrows(IllegalArgumentException.class, () -> acquire(platformName));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        String platformName = mockPlatformName("stream_NoArg_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        assertTrue(stream().anyMatch(item -> item.getPlatform().getName().equals(platformName)));
    }

    // ##################################################################################

    public static class NormalOAuth2ApiFactory
            extends OAuth2ApiFactory<OAuth2PlatformApplication, OAuth2Callback, OAuth2Token, OAuth2User> {

        private final OAuth2Platform platform =
                new OAuth2Platform(mockPlatformName("ServiceProvider"));

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return platform;
        }

        @Override
        public @NotNull OAuth2PlatformApplication initializePlatformApplication(
                @NotNull String applicationName) {
            return mock(OAuth2PlatformApplication.class);
        }

        @Override
        public @NotNull OAuth2Callback initializeCallback(@NotNull JsonNode rawData) {
            return mock(OAuth2Callback.class);
        }

        @Override
        public @NotNull OAuth2ApiBuildAuthUrl createApiBuildAuthUrl(
                @NotNull OAuth2PlatformApplication platformApplication) {
            return mock(OAuth2ApiBuildAuthUrl.class);
        }

        @Override
        public @NotNull OAuth2ApiExchTokenWithCallback<OAuth2Callback, OAuth2Token> createApiExchTokenWithCallback(
                @NotNull OAuth2PlatformApplication platformApplication,
                @NotNull OAuth2HttpClient httpClient) {
            // noinspection unchecked
            return mock(OAuth2ApiExchTokenWithCallback.class);
        }

        @Override
        public @NotNull OAuth2ApiExchUserWithToken<OAuth2Token, OAuth2User> createApiExchUserWithToken(
                @NotNull OAuth2PlatformApplication platformApplication,
                @NotNull OAuth2HttpClient httpClient) {
            // noinspection unchecked
            return mock(OAuth2ApiExchUserWithToken.class);
        }

    }

}

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
package com.github.wautsns.oauth2template.core.utility.http.factory;

import static com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub.Manipulation.register;
import static com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub.Manipulation.withdrawIf;
import static com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub.create;
import static com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub.stream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub.Manipulation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests for {@link OAuth2HttpClientFactoryHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpClientFactoryHubTests {

    @BeforeAll
    static void beforeAll() {
        assertTrue(stream().anyMatch(NormalOAuth2HttpClientFactory.class::isInstance));
    }

    @BeforeEach
    void beforeEach() {
        withdrawIf(NormalOAuth2HttpClientFactory.class::isInstance);
    }

    @AfterAll
    static void afterAll() {
        register(new NormalOAuth2HttpClientFactory());
    }

    // ##################################################################################

    @Test
    void create_NoArg_Normal() {
        register(new NormalOAuth2HttpClientFactory());
        assertDoesNotThrow(() -> create());
    }

    @Test
    void create_NoArg_NoFactoryAvailable() {
        List<OAuth2HttpClientFactory> registered = stream().collect(Collectors.toList());
        withdrawIf(item -> true);
        assertThrows(IllegalArgumentException.class, OAuth2HttpClientFactoryHub::create);
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        properties.setImplementation(NormalOAuth2HttpClient.class);
        assertThrows(IllegalArgumentException.class, () -> create(properties));
        registered.forEach(Manipulation::register);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void create_OAuth2HttpClientProperties_Normal() {
        register(new NormalOAuth2HttpClientFactory());
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        assertDoesNotThrow(() -> create(properties));
        properties.setImplementation(NormalOAuth2HttpClient.class);
        assertDoesNotThrow(() -> create(properties));
    }

    @Test
    void create_OAuth2HttpClientProperties_NoFactoryAvailable() {
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        properties.setImplementation(NormalOAuth2HttpClient.class);
        assertThrows(IllegalArgumentException.class, () -> create(properties));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        long countA = stream()
                .filter(NormalOAuth2HttpClientFactory.class::isInstance)
                .count();
        assertEquals(0, countA);
        register(new NormalOAuth2HttpClientFactory());
        long countB = stream()
                .filter(NormalOAuth2HttpClientFactory.class::isInstance)
                .count();
        assertEquals(1, countB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nested
    class ManipulationTests {

        @Test
        void register_OAuth2HttpClientFactory_Normal() {
            NormalOAuth2HttpClientFactory instance = new NormalOAuth2HttpClientFactory();
            assertNull(register(instance));
            assertSame(instance, register(new NormalOAuth2HttpClientFactory()));
        }

        @Test
        void register_OAuth2HttpClientFactory_NullInstance() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> register(null));
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Test
        void withdrawIf_Predicate_Normal() {
            register(new NormalOAuth2HttpClientFactory());
            withdrawIf(NormalOAuth2HttpClientFactory.class::isInstance);
            assertFalse(stream().anyMatch(NormalOAuth2HttpClientFactory.class::isInstance));
        }

        @Test
        void withdrawIf_Predicate_NullFilter() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdrawIf(null));
        }

    }

    // ##################################################################################

    static class NormalOAuth2HttpClient extends OAuth2HttpClient {

        @Override
        protected @NotNull OAuth2HttpResponse execute(
                @NotNull OAuth2HttpMethod method, @NotNull String url,
                @NotNull OAuth2HttpHeaders headers, @Nullable OAuth2HttpEntity<?> entity)
                throws IOException {
            return mock(OAuth2HttpResponse.class);
        }

        public NormalOAuth2HttpClient(@NotNull OAuth2HttpClientProperties properties) {
            super(properties);
        }

    }

    public static class NormalOAuth2HttpClientFactory extends OAuth2HttpClientFactory {

        @Override
        public @NotNull Class<? extends OAuth2HttpClient> getTargetClass() {
            return NormalOAuth2HttpClient.class;
        }

        @Override
        public @NotNull OAuth2HttpClient create(@NotNull OAuth2HttpClientProperties properties) {
            return new NormalOAuth2HttpClient(properties);
        }

    }

}

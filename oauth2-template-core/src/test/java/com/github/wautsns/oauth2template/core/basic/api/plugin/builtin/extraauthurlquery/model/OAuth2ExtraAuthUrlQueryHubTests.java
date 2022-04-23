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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model;

import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.Manipulation.register;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.Manipulation.withdraw;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.Manipulation.withdrawIf;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.find;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.optional;
import static com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2UrlQuery;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2ExtraAuthUrlQueryHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ExtraAuthUrlQueryHubTests {

    private static final String MOCK_NAME_PREFIX =
            OAuth2ExtraAuthUrlQueryHubTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "platform#" + name;
    }

    private static String mockIdentifier(String name) {
        return MOCK_NAME_PREFIX + "identifier#" + name;
    }

    // ##################################################################################

    @Test
    void find_String$String_Normal() {
        String platformName = mockPlatformName("find_String$String_Normal");
        String identifier = mockIdentifier("find_String$String_Normal");
        OAuth2ExtraAuthUrlQuery instanceA = find(platformName, identifier);
        assertNull(instanceA);
        NormalOAuth2ExtraAuthUrlQuery instanceB =
                new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier);
        register(instanceB);
        OAuth2ExtraAuthUrlQuery instanceC = find(platformName, identifier);
        assertSame(instanceB, instanceC);
    }

    @Test
    void find_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null, ""));
    }

    @Test
    void find_String$String_NullIdentifier() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find("", null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_String$String_Normal() {
        String platformName = mockPlatformName("optional_String$String_Normal");
        String identifier = mockIdentifier("optional_String$String_Normal");
        Optional<OAuth2ExtraAuthUrlQuery> optionalA = optional(platformName, identifier);
        assertFalse(optionalA.isPresent());
        register(new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier));
        Optional<OAuth2ExtraAuthUrlQuery> optionalB = optional(platformName, identifier);
        assertTrue(optionalB.isPresent());
        assertEquals(platformName, optionalB.get().getPlatform().getName());
        assertEquals(identifier, optionalB.get().getIdentifier());
    }

    @Test
    void optional_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null, ""));
    }

    @Test
    void optional_String$String_NullIdentifier() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional("", null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_String$String_Normal() {
        String platformName = mockPlatformName("acquire_String$String_Normal");
        String identifier = mockIdentifier("acquire_String$String_Normal");
        register(new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier));
        OAuth2ExtraAuthUrlQuery instance = acquire(platformName, identifier);
        assertNotNull(instance);
        assertEquals(platformName, instance.getPlatform().getName());
        assertEquals(identifier, instance.getIdentifier());
    }

    @Test
    void acquire_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null, ""));
    }

    @Test
    void acquire_String$String_NullIdentifier() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire("", null));
    }

    @Test
    void acquire_String$String_NotExists() {
        String platformName = mockPlatformName("acquire_String$String_NotExists");
        String identifier = mockIdentifier("acquire_String$String_NotExists");
        assertThrows(IllegalArgumentException.class, () -> acquire(platformName, identifier));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        String platformName = mockPlatformName("stream_NoArg_Normal");
        String identifier = mockIdentifier("stream_NoArg_Normal");
        register(new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier));
        assertTrue(stream().anyMatch(item -> {
            return item.getPlatform().getName().equals(platformName)
                    && item.getIdentifier().equals(identifier);
        }));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nested
    class ManipulationTests {

        @Test
        void register_OAuth2ExtraAuthUrlQuery_Normal() {
            String platformName = mockPlatformName("register_OAuth2ExtraAuthUrlQuery_Normal");
            String identifier = mockIdentifier("register_OAuth2ExtraAuthUrlQuery_Normal");
            NormalOAuth2ExtraAuthUrlQuery instanceA =
                    new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier);
            assertNull(register(instanceA));
            NormalOAuth2ExtraAuthUrlQuery instanceB =
                    new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier);
            assertSame(instanceA, register(instanceB));
        }

        @Test
        void register_OAuth2ExtraAuthUrlQuery_NullInstance() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> register(null));
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Test
        void withdraw_String$String_Normal() {
            String platformName = mockPlatformName("withdraw_String$String_Normal");
            String identifier = mockIdentifier("withdraw_String$String_Normal");
            OAuth2ExtraAuthUrlQuery previousA = withdraw(platformName, identifier);
            assertNull(previousA);
            NormalOAuth2ExtraAuthUrlQuery instance =
                    new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier);
            register(instance);
            OAuth2ExtraAuthUrlQuery previousB = withdraw(platformName, identifier);
            assertSame(instance, previousB);
            assertNull(find(platformName, identifier));
        }

        @Test
        void withdraw_String$String_NullPlatformName() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdraw(null, ""));
        }

        @Test
        void withdraw_String$String_NullIdentifier() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdraw("", null));
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Test
        void withdrawIf_Predicate_Normal() {
            String platformName = mockPlatformName("withdraw_String$String_Normal");
            String identifier = mockIdentifier("withdraw_String$String_Normal");
            register(new NormalOAuth2ExtraAuthUrlQuery(platformName, identifier));
            assertNotNull(find(platformName, identifier));
            withdrawIf(item -> item.getPlatform().getName().equals(platformName));
            assertNull(find(platformName, identifier));
        }

        @Test
        void withdrawIf_Predicate_NullFilter() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdrawIf(null));
        }

    }

    // ##################################################################################

    static class NormalOAuth2ExtraAuthUrlQuery extends OAuth2ExtraAuthUrlQuery {

        private final OAuth2Platform platform;

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return platform;
        }

        @Override
        public void encodeAndSetToQuery(@NotNull OAuth2UrlQuery query) {
            query.encodeAndSet("identifier", getIdentifier());
        }

        public NormalOAuth2ExtraAuthUrlQuery(
                @NotNull String platformName, @NotNull String identifier) {
            super(identifier);
            this.platform = new OAuth2Platform(platformName);
        }

    }

}

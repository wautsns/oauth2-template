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
package com.github.wautsns.oauth2template.core.basic.model.platform;

import static com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2PlatformHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2PlatformHub.find;
import static com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2PlatformHub.optional;
import static com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2PlatformHub.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;

import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2PlatformHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2PlatformHubTests {

    private static final String MOCK_NAME_PREFIX = OAuth2PlatformHubTests.class.getName() + "#";

    private static String mockName(String name) {
        return MOCK_NAME_PREFIX + "#" + name;
    }

    private static void registerMockedOAuth2ApiFactory(String name) {
        OAuth2Platform platform = new OAuth2Platform(name);
        OAuth2ApiFactory<?, ?, ?, ?> factory = spy(OAuth2ApiFactory.class);
        doReturn(platform).when(factory).getPlatform();
        OAuth2ApiFactoryHub.Manipulation.register(factory);
    }

    // ##################################################################################

    @Test
    void find_String_Normal() {
        String name = mockName("find_String_Normal");
        OAuth2Platform instanceA = find(name);
        assertNull(instanceA);
        registerMockedOAuth2ApiFactory(name);
        OAuth2Platform instanceB = find(name);
        assertNotNull(instanceB);
        assertEquals(name, instanceB.getName());
    }

    @Test
    void find_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_String_Normal() {
        String name = mockName("optional_String_Normal");
        Optional<OAuth2Platform> optionalA = optional(name);
        assertFalse(optionalA.isPresent());
        registerMockedOAuth2ApiFactory(name);
        Optional<OAuth2Platform> optionalB = optional(name);
        assertTrue(optionalB.isPresent());
        assertEquals(name, optionalB.get().getName());
    }

    @Test
    void optional_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_String_Normal() {
        String name = mockName("acquire_String_Normal");
        registerMockedOAuth2ApiFactory(name);
        OAuth2Platform instance = acquire(name);
        assertNotNull(instance);
        assertEquals(name, instance.getName());
    }

    @Test
    void acquire_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null));
    }

    @Test
    void acquire_String_NotExists() {
        String name = mockName("acquire_String_NotExists");
        assertThrows(IllegalArgumentException.class, () -> acquire(name));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        String name = mockName("stream_NoArg_Normal");
        registerMockedOAuth2ApiFactory(name);
        assertTrue(stream().anyMatch(item -> item.getName().equals(name)));
    }

}

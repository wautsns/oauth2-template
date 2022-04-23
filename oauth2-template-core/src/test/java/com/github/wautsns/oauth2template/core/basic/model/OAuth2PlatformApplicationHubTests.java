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
package com.github.wautsns.oauth2template.core.basic.model;

import static com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplicationHub.Manipulation.registerIfAbsent;
import static com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplicationHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplicationHub.find;
import static com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplicationHub.optional;
import static com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplicationHub.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2Application;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2PlatformApplicationHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2PlatformApplicationHubTests {

    private static final String MOCK_NAME_PREFIX =
            OAuth2PlatformApplicationHubTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "platform#" + name;
    }

    private static String mockApplicationName(String name) {
        return MOCK_NAME_PREFIX + "application#" + name;
    }

    private static void registerMockedOAuth2ApiFactory(String platformName) {
        OAuth2Platform platform = new OAuth2Platform(platformName);
        // noinspection rawtypes
        OAuth2ApiFactory factory = spy(OAuth2ApiFactory.class);
        doReturn(platform).when(factory).getPlatform();
        doAnswer(inv -> {
            String applicationName = inv.getArgument(0);
            OAuth2Application application =
                    OAuth2ApplicationHub.Manipulation.registerIfAbsent(applicationName);
            OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
            doReturn(platform).when(platformApplication).getPlatform();
            doReturn(application).when(platformApplication).getApplication();
            doReturn(OAuth2Context.hashMap()).when(platformApplication).getContext();
            return platformApplication;
        }).when(factory).initializePlatformApplication(any());
        OAuth2ApiFactoryHub.Manipulation.register(factory);
    }

    // ##################################################################################

    @Test
    void find_String$String_Normal() {
        String platformName = mockPlatformName("find_String$String_Normal");
        String applicationName = mockApplicationName("find_String$String_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        OAuth2PlatformApplication instanceA = find(platformName, applicationName);
        assertNull(instanceA);
        registerIfAbsent(platformName, applicationName);
        OAuth2PlatformApplication instanceB = find(platformName, applicationName);
        assertNotNull(instanceB);
        assertEquals(platformName, instanceB.getPlatform().getName());
        assertEquals(applicationName, instanceB.getApplication().getName());
    }

    @Test
    void find_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null, ""));
    }

    @Test
    void find_String$String_NullApplicationName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find("", null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_String$String_Normal() {
        String platformName = mockPlatformName("optional_String$String_Normal");
        String applicationName = mockApplicationName("optional_String$String_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        Optional<OAuth2PlatformApplication> optionalA = optional(platformName, applicationName);
        assertFalse(optionalA.isPresent());
        registerIfAbsent(platformName, applicationName);
        Optional<OAuth2PlatformApplication> optionalB = optional(platformName, applicationName);
        assertTrue(optionalB.isPresent());
        assertEquals(platformName, optionalB.get().getPlatform().getName());
        assertEquals(applicationName, optionalB.get().getApplication().getName());
    }

    @Test
    void optional_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null, ""));
    }

    @Test
    void optional_String$String_NullApplicationName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional("", null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_String$String_Normal() {
        String platformName = mockPlatformName("acquire_String$String_Normal");
        String applicationName = mockApplicationName("acquire_String$String_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        registerIfAbsent(platformName, applicationName);
        OAuth2PlatformApplication instance = acquire(platformName, applicationName);
        assertNotNull(instance);
        assertEquals(platformName, instance.getPlatform().getName());
        assertEquals(applicationName, instance.getApplication().getName());
    }

    @Test
    void acquire_String$String_NullPlatformName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null, ""));
    }

    @Test
    void acquire_String$String_NullApplicationName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire("", null));
    }

    @Test
    void acquire_String$String_NotExists() {
        String platformName = mockPlatformName("acquire_String$String_NotExists");
        String applicationName = mockApplicationName("acquire_String$String_NotExists");
        assertThrows(IllegalArgumentException.class, () -> acquire(platformName, applicationName));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        String platformName = mockPlatformName("stream_NoArg_Normal");
        String applicationName = mockApplicationName("stream_NoArg_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        registerIfAbsent(platformName, applicationName);
        assertTrue(stream().anyMatch(item -> {
            return (item.getPlatform().getName().equals(platformName))
                    && applicationName.equals(item.getApplication().getName());
        }));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nested
    class ManipulationTests {

        @Test
        void registerIfAbsent_String$String_Normal() {
            String platformName = mockPlatformName("registerIfAbsent_String$String_Normal");
            String applicationName = mockApplicationName("registerIfAbsent_String$String_Normal");
            registerMockedOAuth2ApiFactory(platformName);
            OAuth2PlatformApplication instance1 = registerIfAbsent(platformName, applicationName);
            OAuth2PlatformApplication instance2 = registerIfAbsent(platformName, applicationName);
            assertSame(instance1, instance2);
            assertEquals(platformName, instance1.getPlatform().getName());
            assertEquals(applicationName, instance1.getApplication().getName());
        }

        @Test
        void registerIfAbsent_String$String_NullPlatformName() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> registerIfAbsent(null, ""));
        }

        @Test
        void registerIfAbsent_String$String_NullApplicationName() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> registerIfAbsent("", null));
        }

    }

}

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
package com.github.wautsns.oauth2template.core.basic.template;

import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.Manipulation.register;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.Manipulation.withdraw;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.Manipulation.withdrawIf;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.find;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.optional;
import static com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub.stream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2Application;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2TemplateHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2TemplateHubTests {

    private static OAuth2PlatformApplication mockPlatformApplication() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Platform platform = new OAuth2Platform("name");
        doReturn(platform).when(platformApplication).getPlatform();
        OAuth2Application application = mock(OAuth2Application.class);
        doReturn("name").when(application).getName();
        doReturn(application).when(platformApplication).getApplication();
        return platformApplication;
    }

    private static void registerMockedOAuth2Template(
            OAuth2PlatformApplication platformApplication) {
        OAuth2Template<?, ?, ?, ?> template = mock(OAuth2Template.class);
        doReturn(platformApplication).when(template).getPlatformApplication();
        register(template);
    }

    // ##################################################################################

    @Test
    void find_OAuth2PlatformApplication_Normal() {
        OAuth2PlatformApplication platformApplication = mockPlatformApplication();
        OAuth2Template<?, ?, ?, ?> instanceA = find(platformApplication);
        assertNull(instanceA);
        registerMockedOAuth2Template(platformApplication);
        OAuth2Template<?, ?, ?, ?> instanceB = find(platformApplication);
        assertNotNull(instanceB);
        assertSame(platformApplication, instanceB.getPlatformApplication());
    }

    @Test
    void find_OAuth2PlatformApplication_NullPlatformApplication() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_OAuth2PlatformApplication_Normal() {
        OAuth2PlatformApplication platformApplication = mockPlatformApplication();
        Optional<OAuth2Template<?, ?, ?, ?>> optionalA = optional(platformApplication);
        assertFalse(optionalA.isPresent());
        registerMockedOAuth2Template(platformApplication);
        Optional<OAuth2Template<?, ?, ?, ?>> optionalB = optional(platformApplication);
        assertTrue(optionalB.isPresent());
        assertSame(platformApplication, optionalB.get().getPlatformApplication());
    }

    @Test
    void optional_OAuth2PlatformApplication_NullPlatformApplication() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_OAuth2PlatformApplication_Normal() {
        OAuth2PlatformApplication platformApplication = mockPlatformApplication();
        assertThrows(IllegalArgumentException.class, () -> acquire(platformApplication));
        registerMockedOAuth2Template(platformApplication);
        OAuth2Template<?, ?, ?, ?> instance = acquire(platformApplication);
        assertSame(platformApplication, instance.getPlatformApplication());
    }

    @Test
    void acquire_OAuth2PlatformApplication_NullPlatformApplication() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null));
    }

    @Test
    void acquire_OAuth2PlatformApplication_NotExists() {
        OAuth2PlatformApplication platformApplication = mockPlatformApplication();
        assertThrows(IllegalArgumentException.class, () -> acquire(platformApplication));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        OAuth2PlatformApplication platformApplication = mockPlatformApplication();
        registerMockedOAuth2Template(platformApplication);
        assertTrue(stream().anyMatch(item -> {
            return item.getPlatformApplication() == platformApplication;
        }));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nested
    class ManipulationTests {

        @Test
        void register_OAuth2Template_Normal() {
            OAuth2PlatformApplication platformApplication = mockPlatformApplication();
            OAuth2Template<?, ?, ?, ?> instanceA = mock(OAuth2Template.class);
            doReturn(platformApplication).when(instanceA).getPlatformApplication();
            OAuth2Template<?, ?, ?, ?> previousA = register(instanceA);
            assertNull(previousA);
            OAuth2Template<?, ?, ?, ?> instanceB = mock(OAuth2Template.class);
            doReturn(platformApplication).when(instanceB).getPlatformApplication();
            OAuth2Template<?, ?, ?, ?> previousB = register(instanceB);
            assertSame(instanceA, previousB);
        }

        @Test
        void register_OAuth2Template_NullInstance() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> register(null));
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Test
        void withdraw_OAuth2PlatformApplication_Normal() {
            OAuth2PlatformApplication platformApplication = mockPlatformApplication();
            OAuth2Template<?, ?, ?, ?> previousA = withdraw(platformApplication);
            assertNull(previousA);
            OAuth2Template<?, ?, ?, ?> instance = mock(OAuth2Template.class);
            doReturn(platformApplication).when(instance).getPlatformApplication();
            register(instance);
            OAuth2Template<?, ?, ?, ?> previousB = withdraw(platformApplication);
            assertSame(instance, previousB);
            assertNull(find(platformApplication));
        }

        @Test
        void withdraw_OAuth2PlatformApplication_NullPlatformApplication() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdraw(null));
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Test
        void withdrawIf_Predicate_Normal() {
            OAuth2PlatformApplication platformApplication = mockPlatformApplication();
            withdrawIf(item -> item.getPlatformApplication() == platformApplication);
            registerMockedOAuth2Template(platformApplication);
            withdrawIf(item -> item.getPlatformApplication() == platformApplication);
            assertNull(find(platformApplication));
        }

        @Test
        void withdrawIf_Predicate_NullFilter() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> withdrawIf(null));
        }

    }

}

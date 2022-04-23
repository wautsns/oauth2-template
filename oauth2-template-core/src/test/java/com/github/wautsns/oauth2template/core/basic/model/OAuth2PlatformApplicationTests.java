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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2PlatformApplication}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2PlatformApplicationTests {

    private static final String MOCK_NAME_PREFIX =
            OAuth2PlatformApplicationTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "platform#" + name;
    }

    private static String mockApplicationName(String name) {
        return MOCK_NAME_PREFIX + "application#" + name;
    }

    private static void registerMockedOAuth2ApiFactory(String platformName) {
        OAuth2Platform platform = new OAuth2Platform(platformName);
        OAuth2ApiFactory<?, ?, ?, ?> factory = spy(OAuth2ApiFactory.class);
        doReturn(platform).when(factory).getPlatform();
        OAuth2ApiFactoryHub.Manipulation.register(factory);
    }

    // ##################################################################################

    @Test
    void OAuth2PlatformApplication_OAuth2Platform$String_Normal() {
        String platformName =
                mockPlatformName("OAuth2PlatformApplication_OAuth2Platform$String_Normal");
        String applicationName =
                mockApplicationName("OAuth2PlatformApplication_OAuth2Platform$String_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        NormalOAuth2PlatformApplication instance =
                new NormalOAuth2PlatformApplication(platformName, applicationName);
        assertEquals(platformName, instance.getPlatform().getName());
        assertEquals(applicationName, instance.getApplication().getName());
        assertNotNull(instance.getContext());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        String platformName = mockPlatformName("equals_Object_Normal");
        String applicationName = mockApplicationName("equals_Object_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        NormalOAuth2PlatformApplication instanceA =
                new NormalOAuth2PlatformApplication(platformName, applicationName);
        NormalOAuth2PlatformApplication instanceB =
                new NormalOAuth2PlatformApplication(platformName, applicationName);
        assertNotEquals(instanceA, instanceB);
    }

    @Test
    void hashCode_NoArg_Normal() {
        String platformName = mockPlatformName("hashCode_NoArg_Normal");
        String applicationName = mockApplicationName("hashCode_NoArg_Normal");
        registerMockedOAuth2ApiFactory(platformName);
        NormalOAuth2PlatformApplication instance =
                new NormalOAuth2PlatformApplication(platformName, applicationName);
        assertDoesNotThrow(instance::hashCode);
    }

    // ##################################################################################

    static class NormalOAuth2PlatformApplication extends OAuth2PlatformApplication {

        @Override
        public void validate() {}

        public NormalOAuth2PlatformApplication(
                @NotNull String platformName, @NotNull String applicationName) {
            super(platformName, applicationName);
        }

    }

}

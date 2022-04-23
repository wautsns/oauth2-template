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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2ApiFactory}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiFactoryTests {

    private static final String MOCK_NAME_PREFIX = OAuth2ApiFactoryTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
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
    void createApiRefreshToken_OAuth2PlatformApplication$OAuth2HttpClient_Normal() {
        String platformName =
                mockPlatformName(
                        "createApiRefreshToken_OAuth2PlatformApplication$OAuth2HttpClient_Normal"
                );
        registerMockedOAuth2ApiFactory(platformName);
        // noinspection rawtypes
        OAuth2ApiFactory instance = OAuth2ApiFactoryHub.acquire(platformName);
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2HttpClient httpClient = mock(OAuth2HttpClient.class);
        // noinspection unchecked
        assertNull(instance.createApiRefreshToken(platformApplication, httpClient));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void createApiRevokeAuth_OAuth2PlatformApplication$OAuth2HttpClient_Normal() {
        String platformName =
                mockPlatformName(
                        "createApiRevokeAuth_OAuth2PlatformApplication$OAuth2HttpClient_Normal"
                );
        registerMockedOAuth2ApiFactory(platformName);
        // noinspection rawtypes
        OAuth2ApiFactory instance = OAuth2ApiFactoryHub.acquire(platformName);
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2HttpClient httpClient = mock(OAuth2HttpClient.class);
        // noinspection unchecked
        assertNull(instance.createApiRevokeAuth(platformApplication, httpClient));
    }

}

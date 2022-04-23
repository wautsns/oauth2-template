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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;
import com.github.wautsns.oauth2template.core.basic.api.model.callback.OAuth2Callback;
import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.basic.api.model.user.OAuth2User;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.OAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.ExtraAuthUrlQueryOAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher.TokenAutoRefresherOAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener.TokenEventListenerOAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener.interceptor.TokenEventListenerOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2Application;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link OAuth2Template}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2TemplateTests {

    private static final String MOCK_NAME_PREFIX = OAuth2TemplateTests.class.getName() + "#";

    private static OAuth2PlatformApplication mockPlatformApplication(String method) {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        String platformName = MOCK_NAME_PREFIX + "platform#" + method;
        OAuth2Platform platform = new OAuth2Platform(platformName);
        doReturn(platform).when(platformApplication).getPlatform();
        String applicationName = MOCK_NAME_PREFIX + "application#" + method;
        OAuth2Application application =
                OAuth2ApplicationHub.Manipulation.registerIfAbsent(applicationName);
        doReturn(application).when(platformApplication).getApplication();
        return platformApplication;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerMockedOAuth2ApiFactory(
            OAuth2Platform platform, boolean isApiRefreshTokenSupported,
            boolean isApiRevokeAuthSupported) {
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
            doReturn(null).when(platformApplication).validate();
            return platformApplication;
        }).when(factory).initializePlatformApplication(any());
        doAnswer(inv -> {
            JsonNode rawData = inv.getArgument(0);
            return new OAuth2Callback(rawData) {
                @Override
                public @NotNull OAuth2Platform getPlatform() {
                    return platform;
                }

                @Override
                public @Nullable String getCode() {
                    return getString("code");
                }

                @Override
                public @Nullable String getState() {
                    return getString("state");
                }
            };
        }).when(factory).initializeCallback(any());
        doReturn((OAuth2ApiBuildAuthUrl) context -> new OAuth2Url("url", 4))
                .when(factory).createApiBuildAuthUrl(any());
        doReturn((OAuth2ApiExchTokenWithCallback<?, ?>) callback -> mock(OAuth2Token.class))
                .when(factory).createApiExchTokenWithCallback(any(), any());
        doReturn((OAuth2ApiExchUserWithToken<?, ?>) token -> mock(OAuth2User.class))
                .when(factory).createApiExchUserWithToken(any(), any());
        if (!isApiRefreshTokenSupported) {
            doReturn(null).when(factory).createApiRefreshToken(any(), any());
        } else {
            doReturn((OAuth2ApiRefreshToken<?>) token -> mock(OAuth2Token.class))
                    .when(factory).createApiRefreshToken(any(), any());
        }
        if (!isApiRevokeAuthSupported) {
            doReturn(null).when(factory).createApiRevokeAuth(any(), any());
        } else {
            doReturn((OAuth2ApiRevokeAuth<?>) token -> null)
                    .when(factory).createApiRevokeAuth(any(), any());
        }
        OAuth2ApiFactoryHub.Manipulation.register(factory);
    }

    // ##################################################################################

    @Test
    void OAuth2Template_OAuth2PlatformApplication$OAuth2HttpClient$List_WithoutPlugins()
            throws Exception {
        OAuth2PlatformApplication platformApplication =
                mockPlatformApplication(
                        "OAuth2Template_OAuth2PlatformApplication$OAuth2HttpClient$List_WithoutPlugins"
                );
        registerMockedOAuth2ApiFactory(platformApplication.getPlatform(), false, false);
        NormalOAuth2Template template = new NormalOAuth2Template(platformApplication);
        new NormalOAuth2Template(platformApplication, mock(OAuth2HttpClient.class));
        assertSame(platformApplication, template.getPlatformApplication());
        template.buildAuthUrl();
        template.exchUserWithCallback(new ObjectMapper().createObjectNode());
        assertFalse(template.isApiRefreshTokenSupported());
        assertThrows(UnsupportedOperationException.class, () -> {
            template.refreshToken(mock(OAuth2Token.class));
        });
        assertFalse(template.isApiRevokeAuthSupported());
        assertThrows(UnsupportedOperationException.class, () -> {
            template.revokeAuth(mock(OAuth2Token.class));
        });
    }

    @Test
    void OAuth2Template_OAuth2PlatformApplication$OAuth2HttpClient$List_WithPlugins()
            throws Exception {
        OAuth2PlatformApplication platformApplication =
                mockPlatformApplication(
                        "OAuth2Template_OAuth2PlatformApplication$OAuth2HttpClient$List_WithPlugins"
                );
        registerMockedOAuth2ApiFactory(platformApplication.getPlatform(), true, true);
        NormalOAuth2Template template =
                new NormalOAuth2Template(
                        platformApplication,
                        Arrays.asList(
                                new ExtraAuthUrlQueryOAuth2ApiPlugin(),
                                new TokenAutoRefresherOAuth2ApiPlugin(),
                                new TokenEventListenerOAuth2ApiPlugin(Arrays.asList(
                                        spy(TokenEventListenerOAuth2ApiInterceptor.class)
                                ))
                        )
                );
        assertSame(platformApplication, template.getPlatformApplication());
        template.buildAuthUrl();
        template.exchUserWithCallback(new ObjectMapper().createObjectNode());
        assertTrue(template.isApiRefreshTokenSupported());
        template.refreshToken(mock(OAuth2Token.class));
        assertTrue(template.isApiRevokeAuthSupported());
        template.revokeAuth(mock(OAuth2Token.class));
    }

    // ##################################################################################

    static class NormalOAuth2Template
            extends OAuth2Template<OAuth2PlatformApplication, OAuth2Callback, OAuth2Token, OAuth2User> {

        public NormalOAuth2Template(@NotNull OAuth2PlatformApplication platformApplication) {
            super(platformApplication);
        }

        public NormalOAuth2Template(
                @NotNull OAuth2PlatformApplication platformApplication,
                @NotNull OAuth2HttpClient httpClient) {
            super(platformApplication, httpClient);
        }

        public NormalOAuth2Template(
                @NotNull OAuth2PlatformApplication platformApplication,
                @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
            super(platformApplication, plugins);
        }

        public NormalOAuth2Template(
                @NotNull OAuth2PlatformApplication platformApplication,
                @NotNull OAuth2HttpClient httpClient,
                @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
            super(platformApplication, httpClient, plugins);
        }

    }

}

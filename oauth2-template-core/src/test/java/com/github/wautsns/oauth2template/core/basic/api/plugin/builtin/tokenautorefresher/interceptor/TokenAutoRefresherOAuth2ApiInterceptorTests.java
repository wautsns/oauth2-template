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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.wautsns.oauth2template.core.basic.api.model.OAuth2DataReceived;
import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.basic.template.OAuth2Template;
import com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub;
import com.github.wautsns.oauth2template.core.exception.specific.OAuth2AccessTokenExpiredException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for {@link TokenAutoRefresherOAuth2ApiInterceptor}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class TokenAutoRefresherOAuth2ApiInterceptorTests {

    private static final String MOCK_NAME_PREFIX =
            TokenAutoRefresherOAuth2ApiInterceptorTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "platform#" + name;
    }

    private static OAuth2PlatformApplication registerMockedOAuth2Template(
            String platformName, boolean apiRefreshTokenSupported) throws Exception {
        // noinspection rawtypes
        OAuth2Template oauth2template = mock(OAuth2Template.class);
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Platform platform = new OAuth2Platform(platformName);
        doReturn(platform).when(platformApplication).getPlatform();
        doReturn(platformApplication).when(oauth2template).getPlatformApplication();
        doReturn(apiRefreshTokenSupported).when(oauth2template).isApiRefreshTokenSupported();
        if (apiRefreshTokenSupported) {
            // noinspection unchecked
            doAnswer(inv -> {
                OAuth2Token oldToken = inv.getArgument(0);
                OAuth2Token newToken = mock(OAuth2Token.class);
                doReturn("newAccessToken").when(newToken).getAccessToken();
                doReturn(oldToken.getRefreshToken()).when(newToken).getRefreshToken();
                return newToken;
            }).when(oauth2template).refreshToken(any());
        }
        OAuth2TemplateHub.Manipulation.register(oauth2template);
        return platformApplication;
    }

    private static OAuth2ApiInvocation mockOAuth2ApiInvocation(
            OAuth2PlatformApplication platformApplication) throws Exception {
        OAuth2ApiInvocation invocation = mock(OAuth2ApiInvocation.class);
        doReturn(platformApplication).when(invocation).getPlatformApplication();
        OAuth2Token oldToken = mock(OAuth2Token.class);
        doReturn("oldAccessToken").when(oldToken).getAccessToken();
        doReturn("refreshToken").when(oldToken).getRefreshToken();
        doCallRealMethod().when(invocation).getInput();
        doCallRealMethod().when(invocation).setInput(any());
        invocation.setInput(oldToken);
        doAnswer(inv -> {
            OAuth2Token token = (OAuth2Token) invocation.getInput();
            if ("newAccessToken".equals(token.getAccessToken())) {
                return token;
            } else {
                OAuth2DataReceived dataReceived = mock(OAuth2DataReceived.class);
                throw new OAuth2AccessTokenExpiredException(null, dataReceived);
            }
        }).when(invocation).proceed();
        return invocation;
    }

    // ##################################################################################

    @Test
    void invoke_OAuth2ApiInvocation_Normal() throws Exception {
        TokenAutoRefresherOAuth2ApiInterceptor instance =
                new TokenAutoRefresherOAuth2ApiInterceptor();
        OAuth2ApiInvocation invocation = mock(OAuth2ApiInvocation.class);
        OAuth2Token token = mock(OAuth2Token.class);
        doReturn(token).when(invocation).proceed();
        assertSame(token, instance.invoke(invocation));
    }

    @Test
    void invoke_OAuth2ApiInvocation_RefreshTokenApiIsNotSupported() throws Exception {
        TokenAutoRefresherOAuth2ApiInterceptor instance =
                new TokenAutoRefresherOAuth2ApiInterceptor();
        String platformName =
                mockPlatformName("invoke_OAuth2ApiInvocation_RefreshTokenApiIsSupported");
        OAuth2PlatformApplication platformApplication =
                registerMockedOAuth2Template(platformName, false);
        OAuth2ApiInvocation invocation = mockOAuth2ApiInvocation(platformApplication);
        assertThrows(OAuth2AccessTokenExpiredException.class, () -> instance.invoke(invocation));
        verify(invocation, times(1)).proceed();
    }

    @Test
    void invoke_OAuth2ApiInvocation_RefreshTokenApiIsSupported() throws Exception {
        TokenAutoRefresherOAuth2ApiInterceptor instance =
                new TokenAutoRefresherOAuth2ApiInterceptor();
        String platformName =
                mockPlatformName("invoke_OAuth2ApiInvocation_RefreshTokenApiIsSupported");
        OAuth2PlatformApplication platformApplication =
                registerMockedOAuth2Template(platformName, true);
        OAuth2ApiInvocation invocation = mockOAuth2ApiInvocation(platformApplication);
        OAuth2Token oldToken = (OAuth2Token) invocation.getInput();
        OAuth2Token newToken = (OAuth2Token) instance.invoke(invocation);
        assertEquals("newAccessToken", newToken.getAccessToken());
        assertEquals(oldToken.getRefreshToken(), newToken.getRefreshToken());
        verify(invocation, times(2)).proceed();
    }

    @Test
    void invoke_OAuth2ApiInvocation_NullInvocation() {
        TokenAutoRefresherOAuth2ApiInterceptor instance =
                new TokenAutoRefresherOAuth2ApiInterceptor();
        // noinspection ConstantConditions
        Assertions.assertThrows(NullPointerException.class, () -> instance.invoke(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void TokenAutoRefresherOAuth2ApiInterceptor_NoArg_Normal() {
        TokenAutoRefresherOAuth2ApiInterceptor instance =
                new TokenAutoRefresherOAuth2ApiInterceptor();
        List<OAuth2ApiInterceptPoint> points = instance.getInterceptPoints();
        assertEquals(1, points.size());
        assertEquals(
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_AUTO_REFRESHER_X_EXCH_USER_WITH_TOKEN,
                points.get(0)
        );
    }

}

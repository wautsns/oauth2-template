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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link TokenEventListenerOAuth2ApiInterceptor}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class TokenEventListenerOAuth2ApiInterceptorTests {

    @Test
    void invoke_OAuth2ApiInvocation_Normal() throws Exception {
        TokenEventListenerOAuth2ApiInterceptor instance =
                spy(TokenEventListenerOAuth2ApiInterceptor.class);
        OAuth2ApiInvocation invocationA = mock(OAuth2ApiInvocation.class);
        doReturn(mock(OAuth2ApiExchTokenWithCallback.class)).when(invocationA).getApi();
        instance.invoke(invocationA);
        verify(instance, times(1)).invokeApiExchTokenWithCallback(any());
        verify(instance, times(0)).invokeApiRefreshToken(any());
        verify(instance, times(0)).invokeApiRevokeAuth(any());
        OAuth2ApiInvocation invocationB = mock(OAuth2ApiInvocation.class);
        doReturn(mock(OAuth2ApiRefreshToken.class)).when(invocationB).getApi();
        instance.invoke(invocationB);
        verify(instance, times(1)).invokeApiExchTokenWithCallback(any());
        verify(instance, times(1)).invokeApiRefreshToken(any());
        verify(instance, times(0)).invokeApiRevokeAuth(any());
        OAuth2ApiInvocation invocationC = mock(OAuth2ApiInvocation.class);
        doReturn(mock(OAuth2ApiRevokeAuth.class)).when(invocationC).getApi();
        instance.invoke(invocationC);
        verify(instance, times(1)).invokeApiExchTokenWithCallback(any());
        verify(instance, times(1)).invokeApiRefreshToken(any());
        verify(instance, times(1)).invokeApiRevokeAuth(any());
    }

    @Test
    void invoke_OAuth2ApiInvocation_NullInvocation() {
        TokenEventListenerOAuth2ApiInterceptor instance =
                spy(TokenEventListenerOAuth2ApiInterceptor.class);
        // noinspection ConstantConditions
        Assertions.assertThrows(NullPointerException.class, () -> instance.invoke(null));
    }

    @Test
    void invoke_OAuth2ApiInvocation_IllegalApi() throws Exception {
        TokenEventListenerOAuth2ApiInterceptor instance =
                spy(TokenEventListenerOAuth2ApiInterceptor.class);
        OAuth2ApiInvocation invocation = mock(OAuth2ApiInvocation.class);
        doReturn(mock(OAuth2Api.class)).when(invocation).getApi();
        assertThrows(IllegalStateException.class, () -> instance.invoke(invocation));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void TokenAutoRefresherOAuth2ApiInterceptor_NoArg_Normal() {
        TokenEventListenerOAuth2ApiInterceptor instance =
                spy(TokenEventListenerOAuth2ApiInterceptor.class);
        List<OAuth2ApiInterceptPoint> points = instance.getInterceptPoints();
        assertEquals(3, points.size());
        assertTrue(points.containsAll(Arrays.asList(
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_EXCH_TOKEN_WITH_CALLBACK,
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_REFRESH_TOKEN,
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_REVOKE_AUTH
        )));
    }

}

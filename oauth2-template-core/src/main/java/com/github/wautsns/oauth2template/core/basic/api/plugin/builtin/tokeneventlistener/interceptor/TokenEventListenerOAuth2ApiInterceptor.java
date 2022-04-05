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

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Arrays;
import java.util.Collections;

/**
 * OAuth2Api interceptor for <em>TokenEventListener</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class TokenEventListenerOAuth2ApiInterceptor extends OAuth2ApiInterceptor {

    @Override
    public final @UnknownNullability Object invoke(@NotNull OAuth2ApiInvocation invocation)
            throws OAuth2Exception {
        OAuth2Api<?, ?> api = invocation.getApi();
        if (api instanceof OAuth2ApiExchTokenWithCallback) {
            return invokeApiExchTokenWithCallback(invocation);
        } else if (api instanceof OAuth2ApiRefreshToken) {
            return invokeApiRefreshToken(invocation);
        } else if (api instanceof OAuth2ApiRevokeAuth) {
            return invokeApiRevokeAuth(invocation);
        } else {
            throw new IllegalStateException();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invoke the given invocation of OAuth2Api: <em>ExchTokenWithCallback</em>.
     *
     * @param invocation an invocation
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     * @see OAuth2ApiExchTokenWithCallback
     */
    protected @NotNull Object invokeApiExchTokenWithCallback(
            @NotNull OAuth2ApiInvocation invocation) throws OAuth2Exception {
        return invocation.proceed();
    }

    /**
     * Invoke the given invocation of OAuth2Api: <em>RefreshToken</em>.
     *
     * @param invocation an invocation
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     * @see OAuth2ApiRefreshToken
     */
    protected @NotNull Object invokeApiRefreshToken(
            @NotNull OAuth2ApiInvocation invocation) throws OAuth2Exception {
        return invocation.proceed();
    }

    /**
     * Invoke the given invocation of OAuth2Api: <em>RevokeAuth</em>.
     *
     * @param invocation an invocation
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     * @see OAuth2ApiRevokeAuth
     */
    protected @Nullable Object invokeApiRevokeAuth(
            @NotNull OAuth2ApiInvocation invocation) throws OAuth2Exception {
        return invocation.proceed();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Construct a new instance. */
    protected TokenEventListenerOAuth2ApiInterceptor() {
        super(Collections.unmodifiableList(Arrays.asList(
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_EXCH_TOKEN_WITH_CALLBACK,
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_REFRESH_TOKEN,
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_EVENT_LISTENER_X_REVOKE_AUTH
        )));
    }

}

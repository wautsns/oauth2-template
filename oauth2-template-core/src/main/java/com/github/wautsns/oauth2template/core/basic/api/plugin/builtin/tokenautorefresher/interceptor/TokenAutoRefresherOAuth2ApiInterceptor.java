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

import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.template.OAuth2Template;
import com.github.wautsns.oauth2template.core.basic.template.OAuth2TemplateHub;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.exception.specific.OAuth2AccessTokenExpiredException;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * OAuth2Api interceptor for <em>TokenAutoRefresher</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class TokenAutoRefresherOAuth2ApiInterceptor extends OAuth2ApiInterceptor {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull Object invoke(@NotNull OAuth2ApiInvocation invocation) throws OAuth2Exception {
        try {
            return invocation.proceed();
        } catch (OAuth2AccessTokenExpiredException e) {
            OAuth2PlatformApplication platformApplication = invocation.getPlatformApplication();
            OAuth2Template oauth2template = OAuth2TemplateHub.acquire(platformApplication);
            if (!oauth2template.isApiRefreshTokenSupported()) {throw e;}
            OAuth2Token oldToken = (OAuth2Token) invocation.getInput();
            OAuth2Token newToken = oauth2template.refreshToken(oldToken);
            invocation.setInput(newToken);
            return invocation.proceed();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Construct a new instance. */
    public TokenAutoRefresherOAuth2ApiInterceptor() {
        super(Collections.singletonList(
                BuiltinOAuth2ApiInterceptorPoints.TOKEN_AUTO_REFRESHER_X_EXCH_USER_WITH_TOKEN
        ));
    }

}

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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.interceptor.ExtraAuthUrlQueryOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher.interceptor.TokenAutoRefresherOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener.TokenEventListenerOAuth2ApiPlugin;

/**
 * Builtin OAuth2Api interceptor points.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class BuiltinOAuth2ApiInterceptorPoints {

    /** {@link ExtraAuthUrlQueryOAuth2ApiInterceptor} */
    public static final OAuth2ApiInterceptPoint EXTRA_AUTH_URL_QUERY_X_BUILD_AUTH_URL =
            new OAuth2ApiInterceptPoint(OAuth2ApiBuildAuthUrl.class, 1_0000);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** {@link TokenEventListenerOAuth2ApiPlugin} */
    public static final OAuth2ApiInterceptPoint TOKEN_EVENT_LISTENER_X_EXCH_TOKEN_WITH_CALLBACK =
            new OAuth2ApiInterceptPoint(OAuth2ApiExchTokenWithCallback.class, 2_0000);
    /** {@link TokenEventListenerOAuth2ApiPlugin} */
    public static final OAuth2ApiInterceptPoint TOKEN_EVENT_LISTENER_X_REFRESH_TOKEN =
            new OAuth2ApiInterceptPoint(OAuth2ApiRefreshToken.class, 2_0000);
    /** {@link TokenEventListenerOAuth2ApiPlugin} */
    public static final OAuth2ApiInterceptPoint TOKEN_EVENT_LISTENER_X_REVOKE_AUTH =
            new OAuth2ApiInterceptPoint(OAuth2ApiRevokeAuth.class, 2_0000);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** {@link TokenAutoRefresherOAuth2ApiInterceptor} */
    public static final OAuth2ApiInterceptPoint TOKEN_AUTO_REFRESHER_X_EXCH_USER_WITH_TOKEN =
            new OAuth2ApiInterceptPoint(OAuth2ApiExchUserWithToken.class, 3_0000);

    // ##################################################################################

    /** No need to instantiate. */
    private BuiltinOAuth2ApiInterceptorPoints() {}

}

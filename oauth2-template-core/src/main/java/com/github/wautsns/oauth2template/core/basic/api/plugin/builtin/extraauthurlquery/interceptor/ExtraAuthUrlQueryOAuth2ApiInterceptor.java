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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.interceptor;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.ExtraAuthUrlQueryOAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQuery;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * OAuth2Api interceptor for <em>ExtraAuthUrlQuery</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class ExtraAuthUrlQueryOAuth2ApiInterceptor extends OAuth2ApiInterceptor {

    @Override
    public @NotNull Object invoke(@NotNull OAuth2ApiInvocation invocation) throws OAuth2Exception {
        OAuth2Url authUrl = (OAuth2Url) invocation.proceed();
        OAuth2Context context = (OAuth2Context) invocation.getInput();
        if (context == null) {return authUrl;}
        String identifier = ExtraAuthUrlQueryOAuth2ApiPlugin.getVariableIdentifier(context);
        if (identifier == null) {return authUrl;}
        String platformName = invocation.getPlatformApplication().getPlatform().getName();
        OAuth2ExtraAuthUrlQuery extra = OAuth2ExtraAuthUrlQueryHub.find(platformName, identifier);
        if (extra == null) {return authUrl;}
        extra.encodeAndSetToQuery(authUrl.getQuery());
        return authUrl;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Construct a new instance. */
    public ExtraAuthUrlQueryOAuth2ApiInterceptor() {
        super(Collections.singletonList(
                BuiltinOAuth2ApiInterceptorPoints.EXTRA_AUTH_URL_QUERY_X_BUILD_AUTH_URL
        ));
    }

}

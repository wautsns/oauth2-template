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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.OAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher.interceptor.TokenAutoRefresherOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

/**
 * OAuth2Api plugin for <em>TokenAutoRefresher</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class TokenAutoRefresherOAuth2ApiPlugin extends OAuth2ApiPlugin {

    @Override
    public boolean isApplicable(
            @NotNull OAuth2PlatformApplication platformApplication,
            boolean isApiRefreshTokenSupported, boolean isApiRevokeAuthSupported) {
        Objects.requireNonNull(platformApplication);
        return isApiRefreshTokenSupported;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Construct a new instance. */
    public TokenAutoRefresherOAuth2ApiPlugin() {
        super(Collections.singletonList(
                new TokenAutoRefresherOAuth2ApiInterceptor()
        ));
    }

}

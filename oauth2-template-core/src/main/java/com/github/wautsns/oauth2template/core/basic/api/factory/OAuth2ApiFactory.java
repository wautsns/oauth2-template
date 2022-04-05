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

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * OAuth2Api factory.
 *
 * @author wautsns
 * @implSpec The created OAuth2Api should only contain requests to platform or take values
 *         directly from input, without combining other OAuth2Api instances (otherwise it may cause
 *         some plugins to fail to take effect).
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2ApiFactory<A extends OAuth2PlatformApplication> {

    /**
     * Return the platform this instance belongs to.
     *
     * @return the platform this instance belongs to
     */
    public abstract @NotNull OAuth2Platform getPlatform();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Create a new OAuth2Api: <em>BuildAuthURL</em>.
     *
     * @param platformApplication a platform application
     * @return a new OAuth2Api
     */
    public abstract @NotNull OAuth2ApiBuildAuthUrl createApiBuildAuthUrl(
            @NotNull A platformApplication);

    /**
     * Create a new OAuth2Api: <em>ExchTokenWithCallback</em>.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @return a new OAuth2Api
     */
    public abstract @NotNull OAuth2ApiExchTokenWithCallback<?, ?> createApiExchTokenWithCallback(
            @NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient);

    /**
     * Create a new OAuth2Api: <em>ExchUserWithToken</em>.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @return a new OAuth2Api
     */
    public abstract @NotNull OAuth2ApiExchUserWithToken<?, ?> createApiExchUserWithToken(
            @NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient);

    /**
     * Create a new OAuth2Api: <em>RefreshToken</em>.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @return a new OAuth2Api, or {@code null} if the platform does not support
     */
    public @Nullable OAuth2ApiRefreshToken<?> createApiRefreshToken(
            @NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient) {
        return null;
    }

    /**
     * Create a new OAuth2Api: <em>RevokeAuth</em>.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @return a new OAuth2Api, or {@code null} if the platform does not support
     */
    public @Nullable OAuth2ApiRevokeAuth<?> createApiRevokeAuth(
            @NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient) {
        return null;
    }

}

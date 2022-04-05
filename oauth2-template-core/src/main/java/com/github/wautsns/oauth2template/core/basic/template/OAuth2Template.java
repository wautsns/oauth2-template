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
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocationFactory;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OAuth2 template.
 *
 * @param <A> the type of platform application
 * @param <C> the type of auth callback URL query
 * @param <T> the type of token
 * @param <U> the type of user
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2Template<A extends OAuth2PlatformApplication, C extends OAuth2Callback, T extends OAuth2Token, U extends OAuth2User> {

    /** Platform application. */
    private final @NotNull A platformApplication;

    /** OAuth2Api: <em>BuildAuthURL</em> */
    private final @NotNull OAuth2ApiBuildAuthUrl apiBuildAuthUrl;
    /** OAuth2Api: <em>ExchTokenWithCallback</em> */
    private final @NotNull OAuth2ApiExchTokenWithCallback<C, T> apiExchTokenWithCallback;
    /** OAuth2Api: <em>ExchUserWithToken</em> */
    private final @NotNull OAuth2ApiExchUserWithToken<T, U> apiExchUserWithToken;
    /** OAuth2Api: <em>RefreshToken</em> */
    private final @Nullable OAuth2ApiRefreshToken<T> apiRefreshToken;
    /** OAuth2Api: <em>RevokeAuth</em> */
    private final @Nullable OAuth2ApiRevokeAuth<T> apiRevokeAuth;

    // ##################################################################################

    /**
     * Return platform application.
     *
     * @return platform application
     */
    public final @NotNull A getPlatformApplication() {
        return platformApplication;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Build a new auth URL.
     *
     * @return a new auth URL
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull OAuth2Url buildAuthUrl() throws OAuth2Exception {
        return buildAuthUrl(null);
    }

    /**
     * Build a new auth URL.
     *
     * @param context an OAuth2 context
     * @return a new auth URL
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull OAuth2Url buildAuthUrl(@Nullable OAuth2Context context)
            throws OAuth2Exception {
        return apiBuildAuthUrl.execute(context);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Exchange a token with the given callback.
     *
     * @param callback an auth callback URL query
     * @return a token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull T exchTokenWithCallback(@NotNull JsonNode callback)
            throws OAuth2Exception {
        return exchTokenWithCallback(initializeCallback(callback));
    }

    /**
     * Exchange a token with the given callback.
     *
     * @param callback an auth callback URL query
     * @return a token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull T exchTokenWithCallback(@NotNull C callback) throws OAuth2Exception {
        return apiExchTokenWithCallback.execute(callback);
    }

    /**
     * Exchange a user with the given token.
     *
     * @param callback an auth callback URL query
     * @return a user
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull U exchUserWithCallback(@NotNull JsonNode callback)
            throws OAuth2Exception {
        T token = exchTokenWithCallback(callback);
        return exchUserWithToken(token);
    }

    /**
     * Initialize an auth callback URL query.
     *
     * @param rawData a raw data received from platform
     * @return an auth callback URL query
     */
    protected abstract @NotNull C initializeCallback(@NotNull JsonNode rawData);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Exchange a user with the given token.
     *
     * @param token a token
     * @return a user
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull U exchUserWithToken(@NotNull T token) throws OAuth2Exception {
        return apiExchUserWithToken.execute(token);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return whether the platform supports OAuth2Api: <em>RefreshToken</em>.
     *
     * @return {@code true} if it does, otherwise {@code false}
     */
    public final boolean isApiRefreshTokenSupported() {
        return apiRefreshToken != null;
    }

    /**
     * Refresh the given token.
     *
     * @param oldToken an old token
     * @return a new token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull T refreshToken(@NotNull T oldToken) throws OAuth2Exception {
        if (apiRefreshToken != null) {
            return apiRefreshToken.execute(oldToken);
        } else {
            throw new UnsupportedOperationException(String.format(
                    "Platform `%s` does not support api `RefreshToken`.",
                    platformApplication.getPlatform().getName()
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return whether the platform supports OAuth2Api: <em>RevokeAuth</em>.
     *
     * @return {@code true} if it does, otherwise {@code false}
     */
    public final boolean isApiRevokeAuthSupported() {
        return apiRevokeAuth != null;
    }

    /**
     * Revoke the authorization using the given token.
     *
     * @param token a token
     * @throws OAuth2Exception if an OAuth2 related error occurs, or failed to revoke
     */
    public final void revokeAuth(@NotNull T token) throws OAuth2Exception {
        if (apiRevokeAuth != null) {
            apiRevokeAuth.execute(token);
        } else {
            throw new UnsupportedOperationException(String.format(
                    "Platform `%s` does not support api `RevokeAuth`.",
                    platformApplication.getPlatform().getName()
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     */
    protected OAuth2Template(@NotNull A platformApplication) {
        this(platformApplication, OAuth2HttpClientFactoryHub.create(), Collections.emptyList());
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     */
    protected OAuth2Template(@NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient) {
        this(platformApplication, httpClient, Collections.emptyList());
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param plugins plugins
     */
    protected OAuth2Template(
            @NotNull A platformApplication, @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        this(platformApplication, OAuth2HttpClientFactoryHub.create(), plugins);
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @param plugins plugins
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected OAuth2Template(
            @NotNull A platformApplication, @NotNull OAuth2HttpClient httpClient,
            @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        this.platformApplication = platformApplication;
        this.platformApplication.validate();
        OAuth2ApiFactory apiFactory =
                OAuth2ApiFactoryHub.acquire(platformApplication.getPlatform().getName());
        OAuth2ApiBuildAuthUrl apiBuildAuthUrl =
                apiFactory.createApiBuildAuthUrl(platformApplication);
        OAuth2ApiExchTokenWithCallback<C, T> apiExchTokenWithCallback =
                apiFactory.createApiExchTokenWithCallback(platformApplication, httpClient);
        OAuth2ApiExchUserWithToken<T, U> apiExchUserWithToken =
                apiFactory.createApiExchUserWithToken(platformApplication, httpClient);
        OAuth2ApiRefreshToken<T> apiRefreshToken =
                apiFactory.createApiRefreshToken(platformApplication, httpClient);
        OAuth2ApiRevokeAuth<T> apiRevokeAuth =
                apiFactory.createApiRevokeAuth(platformApplication, httpClient);
        plugins = plugins.stream()
                .filter(plugin -> {
                    return plugin.isApplicable(
                            platformApplication,
                            apiRefreshToken != null,
                            apiRevokeAuth != null
                    );
                })
                .collect(Collectors.toList());
        if (plugins.isEmpty()) {
            this.apiBuildAuthUrl = apiBuildAuthUrl;
            this.apiExchTokenWithCallback = apiExchTokenWithCallback;
            this.apiExchUserWithToken = apiExchUserWithToken;
            this.apiRefreshToken = apiRefreshToken;
            this.apiRevokeAuth = apiRevokeAuth;
        } else {
            // OAuth2Api: BuildAuthUrl
            OAuth2ApiInvocationFactory aifBuildAuthUrl =
                    new OAuth2ApiInvocationFactory(platformApplication, apiBuildAuthUrl, plugins);
            this.apiBuildAuthUrl = context -> {
                return (OAuth2Url) aifBuildAuthUrl.create(context).proceed();
            };
            // OAuth2Api: ExchTokenWithCallback
            OAuth2ApiInvocationFactory aifExchTokenWithCallback =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiExchTokenWithCallback, plugins
                    );
            this.apiExchTokenWithCallback = token -> {
                return (T) aifExchTokenWithCallback.create(token).proceed();
            };
            // OAuth2Api: ExchUserWithToken
            OAuth2ApiInvocationFactory aifExchUserWithToken =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiExchUserWithToken, plugins
                    );
            this.apiExchUserWithToken = token -> {
                return (U) aifExchUserWithToken.create(token).proceed();
            };
            // OAuth2Api: RefreshToken
            if (apiRefreshToken == null) {
                this.apiRefreshToken = null;
            } else {
                OAuth2ApiInvocationFactory aifRefreshToken =
                        new OAuth2ApiInvocationFactory(
                                platformApplication, apiRefreshToken, plugins
                        );
                this.apiRefreshToken = token -> {
                    return (T) aifRefreshToken.create(token).proceed();
                };
            }
            // OAuth2Api: RevokeAuth
            if (apiRevokeAuth == null) {
                this.apiRevokeAuth = null;
            } else {
                OAuth2ApiInvocationFactory aifRevokeAuth =
                        new OAuth2ApiInvocationFactory(
                                platformApplication, apiRevokeAuth, plugins
                        );
                this.apiRevokeAuth = token -> {
                    aifRevokeAuth.create(token).proceed();
                    return null;
                };
            }
        }
    }

}

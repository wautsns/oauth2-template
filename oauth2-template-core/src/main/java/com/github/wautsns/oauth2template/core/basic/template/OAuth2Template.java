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

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
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
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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

    /** Auth callback URL query initializer. */
    private final @NotNull Function<JsonNode, C> callbackInitialize;

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
        Objects.requireNonNull(callback);
        return exchTokenWithCallback(callbackInitialize.apply(callback));
    }

    /**
     * Exchange a token with the given callback.
     *
     * @param callback an auth callback URL query
     * @return a token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull T exchTokenWithCallback(@NotNull C callback) throws OAuth2Exception {
        Objects.requireNonNull(callback);
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

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Exchange a user with the given token.
     *
     * @param token a token
     * @return a user
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public final @NotNull U exchUserWithToken(@NotNull T token) throws OAuth2Exception {
        Objects.requireNonNull(token);
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
        Objects.requireNonNull(oldToken);
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
        Objects.requireNonNull(token);
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
        Objects.requireNonNull(platformApplication);
        Objects.requireNonNull(httpClient);
        Objects.requireNonNull(plugins);
        this.platformApplication = platformApplication;
        this.platformApplication.validate();
        OAuth2ApiFactory apiFactory =
                OAuth2ApiFactoryHub.acquire(platformApplication.getPlatform().getName());
        this.callbackInitialize = rawData -> (C) apiFactory.initializeCallback(rawData);
        // Initialize raw OAuth2Api[s].
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
        // Intercept OAuth2Api[s].
        plugins = plugins.stream()
                .filter(plugin -> {
                    return plugin.isApplicable(
                            platformApplication, apiRefreshToken != null, apiRevokeAuth != null
                    );
                })
                .collect(Collectors.toList());
        Map<Class<? extends OAuth2Api<?, ?>>, List<OAuth2ApiInterceptor>> interceptorsGroupByApi =
                groupApplicableInterceptorsByApi(plugins);
        List<OAuth2ApiInterceptor> interceptors;
        // ==> Intercept OAuth2ApiBuildAuthUrl.
        interceptors = interceptorsGroupByApi.get(OAuth2ApiBuildAuthUrl.class);
        if (interceptors == null || interceptors.isEmpty()) {
            this.apiBuildAuthUrl = apiBuildAuthUrl;
        } else {
            OAuth2ApiInvocationFactory factory =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiBuildAuthUrl, interceptors
                    );
            this.apiBuildAuthUrl = context -> {
                return (OAuth2Url) factory.create(context).proceed();
            };
        }
        // ==> Intercept OAuth2ApiExchTokenWithCallback.
        interceptors = interceptorsGroupByApi.get(OAuth2ApiExchTokenWithCallback.class);
        if (interceptors == null || interceptors.isEmpty()) {
            this.apiExchTokenWithCallback = apiExchTokenWithCallback;
        } else {
            OAuth2ApiInvocationFactory factory =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiExchTokenWithCallback, interceptors
                    );
            this.apiExchTokenWithCallback = context -> {
                return (T) factory.create(context).proceed();
            };
        }
        // ==> Intercept OAuth2ApiExchUserWithToken.
        interceptors = interceptorsGroupByApi.get(OAuth2ApiExchUserWithToken.class);
        if (interceptors == null || interceptors.isEmpty()) {
            this.apiExchUserWithToken = apiExchUserWithToken;
        } else {
            OAuth2ApiInvocationFactory factory =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiExchUserWithToken, interceptors
                    );
            this.apiExchUserWithToken = context -> {
                return (U) factory.create(context).proceed();
            };
        }
        // ==> Intercept OAuth2ApiRefreshToken.
        interceptors = interceptorsGroupByApi.get(OAuth2ApiRefreshToken.class);
        if (apiRefreshToken == null || interceptors == null || interceptors.isEmpty()) {
            this.apiRefreshToken = apiRefreshToken;
        } else {
            OAuth2ApiInvocationFactory factory =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiRefreshToken, interceptors
                    );
            this.apiRefreshToken = context -> {
                return (T) factory.create(context).proceed();
            };
        }
        // ==> Intercept OAuth2ApiRevokeAuth.
        interceptors = interceptorsGroupByApi.get(OAuth2ApiRevokeAuth.class);
        if (apiRevokeAuth == null || interceptors == null || interceptors.isEmpty()) {
            this.apiRevokeAuth = apiRevokeAuth;
        } else {
            OAuth2ApiInvocationFactory factory =
                    new OAuth2ApiInvocationFactory(
                            platformApplication, apiRevokeAuth, interceptors
                    );
            this.apiRevokeAuth = context -> {
                factory.create(context).proceed();
                return null;
            };
        }
    }

    /**
     * Group applicable interceptors by OAuth2Api.
     *
     * @param plugins plugins
     * @return applicable interceptors by OAuth2Api
     */
    private static @NotNull Map<@NotNull Class<? extends OAuth2Api<?, ?>>, @NotNull List<@NotNull OAuth2ApiInterceptor>> groupApplicableInterceptorsByApi(
            @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        if (plugins.isEmpty()) {return new HashMap<>(0);}
        Map<Class<? extends OAuth2Api<?, ?>>, List<OAuth2ApiInterceptPoint>> temp = new HashMap<>();
        Map<OAuth2ApiInterceptPoint, OAuth2ApiInterceptor> pointToInterceptor = new HashMap<>();
        plugins.stream()
                .flatMap(plugin -> plugin.getInterceptors().stream())
                .forEach(interceptor -> {
                    interceptor.getInterceptPoints().forEach(point -> {
                        OAuth2ApiInterceptor previous = pointToInterceptor.put(point, interceptor);
                        if (previous != null) {
                            throw new IllegalArgumentException(String.format(
                                    "InterceptorPoint should not be reused." +
                                            " interceptorA:`%s`,interceptorB:`%s`",
                                    previous.getClass(), interceptor.getClass()
                            ));
                        }
                        temp.computeIfAbsent(point.getTarget(), k -> new LinkedList<>())
                                .add(point);
                    });
                });
        temp.values().forEach(points -> {
            points.sort(Comparator.comparingInt(OAuth2ApiInterceptPoint::getOrder));
        });
        Map<Class<? extends OAuth2Api<?, ?>>, List<OAuth2ApiInterceptor>> result = new HashMap<>();
        temp.forEach((api, points) -> {
            List<OAuth2ApiInterceptor> interceptors = points.stream()
                    .map(pointToInterceptor::get)
                    .distinct()
                    .collect(Collectors.toList());
            result.put(api, interceptors);
        });
        return result;
    }

}

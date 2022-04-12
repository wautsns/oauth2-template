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
package com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.OAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * OAuth2Api invocation factory.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@SuppressWarnings("rawtypes")
public final class OAuth2ApiInvocationFactory {

    /** Platform application. */
    private final @NotNull OAuth2PlatformApplication platformApplication;
    /** OAuth2Api. */
    private final @NotNull OAuth2Api api;
    /** An interceptor iterator supplier. */
    private final @NotNull Supplier<@NotNull Iterator<@NotNull OAuth2ApiInterceptor>> interceptorIteratorSupplier;

    // ##################################################################################

    /**
     * Create a new invocation.
     *
     * @param input an input
     * @return a new invocation
     */
    public @NotNull OAuth2ApiInvocation create(@UnknownNullability Object input) {
        Iterator<OAuth2ApiInterceptor> interceptorIterator = interceptorIteratorSupplier.get();
        return new OAuth2ApiInvocation(platformApplication, api, interceptorIterator, input);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param api an OAuth2Api
     * @param plugins plugins
     */
    public OAuth2ApiInvocationFactory(
            @NotNull OAuth2PlatformApplication platformApplication,
            @NotNull OAuth2Api<?, ?> api,
            @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        this.platformApplication = Objects.requireNonNull(platformApplication);
        this.api = Objects.requireNonNull(api);
        Objects.requireNonNull(plugins);
        Map<OAuth2ApiInterceptor, OAuth2ApiInterceptPoint> points = new HashMap<>();
        plugins.stream()
                .flatMap(plugin -> plugin.getInterceptors().stream())
                .forEach(interceptor -> {
                    interceptor.getInterceptPoints().stream()
                            .filter(point -> point.getTarget().isInstance(api))
                            .findFirst()
                            .ifPresent(point -> points.put(interceptor, point));
                });
        List<OAuth2ApiInterceptor> interceptors = points.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        this.interceptorIteratorSupplier = interceptors::iterator;
    }

}

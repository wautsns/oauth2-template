/*
 * Copyright 2021 the original author or authors.
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
package com.github.wautsns.oauth2template.core.utility.http.factory;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;
import com.github.wautsns.oauth2template.core.utility.spi.OAuth2ServiceLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2HttpClientFactory}.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>Instances of {@link OAuth2HttpClientFactory} which comply with {@link OAuth2ServiceLoader}
 * specifications will be automatically registered.</li>
 * </ul>
 *
 * @author wautsns
 * @see OAuth2HttpClientFactory
 * @see OAuth2ServiceLoader
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2HttpClientFactoryHub {

    /** Logger. */
    private static final @NotNull Logger log =
            LoggerFactory.getLogger(OAuth2HttpClientFactoryHub.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Data storage. */
    private static final @NotNull Map<@NotNull Class<? extends OAuth2HttpClient>, @NotNull OAuth2HttpClientFactory> STORAGE =
            new ConcurrentHashMap<>();

    static {
        log.info(
                "Automatically register OAuth2HttpClientFactory instances which comply with" +
                        " OAuth2ServiceLoader specifications."
        );
        OAuth2ServiceLoader.load(OAuth2HttpClientFactory.class).iterator()
                .forEachRemaining(instance -> {
                    OAuth2HttpClientFactory previous = Manipulation.register(instance);
                    log.info(
                            "An OAuth2HttpClientFactory instance was successfully registered." +
                                    " target:`{}`,previous:`{}`",
                            instance.getTargetClass(), previous
                    );
                });
        log.info(
                "All OAuth2HttpClientFactory instances which comply with OAuth2ServiceLoader" +
                        " specifications have been registered automatically."
        );
    }

    // ##################################################################################

    /**
     * Create a new http client, using {@link OAuth2HttpClientProperties#DEFAULTS} as properties.
     *
     * @return a new http client
     * @throws IllegalStateException if no factory is available
     */
    public static @NotNull OAuth2HttpClient create() {
        return create(OAuth2HttpClientProperties.DEFAULTS);
    }

    /**
     * Create a new http client with the given properties.
     *
     * @param properties http client properties
     * @return a new http client
     * @throws IllegalStateException if no factory is available
     */
    public static @NotNull OAuth2HttpClient create(@NotNull OAuth2HttpClientProperties properties) {
        Class<? extends OAuth2HttpClient> implementation = properties.getImplementation();
        if (implementation == OAuth2HttpClient.class) {
            return STORAGE.values().stream()
                    .findAny()
                    .orElseThrow(() -> {
                        return new IllegalArgumentException(
                                "No OAuth2HttpClientFactory instance is available."
                        );
                    })
                    .create(properties);
        } else {
            OAuth2HttpClientFactory instance = STORAGE.get(implementation);
            if (instance == null) {
                throw new IllegalArgumentException(String.format(
                        "No OAuth2HttpClientFactory instance can create `%s`.",
                        properties.getImplementation()
                ));
            }
            return instance.create(properties);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a sequential stream with all instances registered in the hub as its source.
     *
     * @return a sequential stream
     */
    public static @NotNull Stream<@NotNull OAuth2HttpClientFactory> stream() {
        return STORAGE.values().stream();
    }

    // ##################################################################################

    /** The utility for manipulating the hub. */
    public static final class Manipulation {

        /**
         * Register the given instance.
         *
         * <ul>
         * <li style="list-style-type:none">########## Notes ###############</li>
         * <li>For the same {@linkplain OAuth2HttpClientFactory#getTargetClass() target class}, only
         * the latest registered instance will take effect.</li>
         * </ul>
         *
         * @param instance an instance
         * @return a previous instance, or {@code null} if not exist
         */
        public static @Nullable OAuth2HttpClientFactory register(
                @NotNull OAuth2HttpClientFactory instance) {
            return STORAGE.put(instance.getTargetClass(), instance);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw all instances that satisfy the given predicate.
         *
         * <ul>
         * <li style="list-style-type:none">########## Notes ###############</li>
         * <li>Errors or runtime exceptions thrown during iteration or by the predicate are relayed
         * to the caller.</li>
         * </ul>
         *
         * @param filter a predicate which returns true for instances to be withdrawn
         */
        public static void withdrawIf(@NotNull Predicate<OAuth2HttpClientFactory> filter) {
            STORAGE.values().removeIf(filter);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2HttpClientFactoryHub() {}

}

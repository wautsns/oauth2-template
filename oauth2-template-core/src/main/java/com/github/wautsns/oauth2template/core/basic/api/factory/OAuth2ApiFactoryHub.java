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
package com.github.wautsns.oauth2template.core.basic.api.factory;

import com.github.wautsns.oauth2template.core.utility.spi.OAuth2ServiceLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2ApiFactory}.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>Instances of {@link OAuth2ApiFactory} which comply with {@link OAuth2ServiceLoader}
 * specifications will be automatically registered.</li>
 * </ul>
 *
 * @author wautsns
 * @see OAuth2ApiFactory
 * @see OAuth2ServiceLoader
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ApiFactoryHub {

    /** Logger. */
    private static final @NotNull Logger log = LoggerFactory.getLogger(OAuth2ApiFactoryHub.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Data storage. */
    private static final @NotNull Map<@NotNull String, @NotNull OAuth2ApiFactory<?, ?, ?, ?>> STORAGE =
            new ConcurrentHashMap<>();

    static {
        log.info(
                "Automatically register OAuth2ApiFactory instances which comply with" +
                        " OAuth2ServiceLoader specifications."
        );
        OAuth2ServiceLoader.load(OAuth2ApiFactory.class).iterator()
                .forEachRemaining(instance -> {
                    OAuth2ApiFactory<?, ?, ?, ?> previous = Manipulation.register(instance);
                    log.info(
                            "An OAuth2ApiFactory instance was successfully registered." +
                                    " platformName:`{}`,previous:`{}`",
                            instance.getPlatform().getName(), previous
                    );
                });
        log.info(
                "All OAuth2ApiFactory instances which comply with OAuth2ServiceLoader" +
                        " specifications have been registered automatically."
        );
    }

    // ##################################################################################

    /**
     * Return an instance of the given platform name.
     *
     * @param platformName a platform name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2ApiFactory<?, ?, ?, ?> find(@NotNull String platformName) {
        Objects.requireNonNull(platformName);
        return STORAGE.get(platformName);
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name.
     *
     * @param platformName a platform name
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2ApiFactory<?, ?, ?, ?>> optional(
            @NotNull String platformName) {
        Objects.requireNonNull(platformName);
        return Optional.ofNullable(STORAGE.get(platformName));
    }

    /**
     * Return an instance of the given platform name.
     *
     * @param platformName a platform name
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2ApiFactory<?, ?, ?, ?> acquire(@NotNull String platformName) {
        Objects.requireNonNull(platformName);
        OAuth2ApiFactory<?, ?, ?, ?> instance = STORAGE.get(platformName);
        if (instance == null) {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2ApiFactory instance of platform `%s` is registered.",
                    platformName
            ));
        }
        return instance;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a sequential stream with all instances registered in the hub as its source.
     *
     * @return a sequential stream
     */
    public static @NotNull Stream<@NotNull OAuth2ApiFactory<?, ?, ?, ?>> stream() {
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
         * <li>For the same platform name, only the latest registered instance will take
         * effect.</li>
         * </ul>
         *
         * @param instance an instance
         * @return a previous instance, or {@code null} if not exist
         */
        public static @Nullable OAuth2ApiFactory<?, ?, ?, ?> register(
                @NotNull OAuth2ApiFactory<?, ?, ?, ?> instance) {
            return STORAGE.put(instance.getPlatform().getName(), instance);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2ApiFactoryHub() {}

}

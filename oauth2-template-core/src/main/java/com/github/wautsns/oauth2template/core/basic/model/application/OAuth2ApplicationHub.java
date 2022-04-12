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
package com.github.wautsns.oauth2template.core.basic.model.application;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2Application}.
 *
 * @author wautsns
 * @see OAuth2Application
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ApplicationHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull String, @NotNull OAuth2Application> STORAGE =
            new ConcurrentHashMap<>();

    // ##################################################################################

    /**
     * Return an instance of the given application name.
     *
     * @param name an application name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2Application find(@NotNull String name) {
        Objects.requireNonNull(name);
        return STORAGE.get(name);
    }

    /**
     * Return an {@code Optional} describing the instance of the given application name.
     *
     * @param name an application name
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2Application> optional(@NotNull String name) {
        Objects.requireNonNull(name);
        return Optional.ofNullable(STORAGE.get(name));
    }

    /**
     * Return an instance of the given platform name.
     *
     * @param name an application name
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2Application acquire(@NotNull String name) {
        Objects.requireNonNull(name);
        OAuth2Application instance = STORAGE.get(name);
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2Application instance of application `%s` is registered.",
                    name
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a sequential stream with all instances registered in the hub as its source.
     *
     * @return a sequential stream
     */
    public static @NotNull Stream<@NotNull OAuth2Application> stream() {
        return STORAGE.values().stream();
    }

    // ##################################################################################

    /** The utility for manipulating the hub. */
    public static final class Manipulation {

        /**
         * Return an instance of the given application name if exists, otherwise a new instance will
         * be registered and returned.
         *
         * <ul>
         * <li style="list-style-type:none">########## Notes ###############</li>
         * <li>There is currently no direct method to remove registered instances, and it is
         * <em>highly not recommended</em> to remove them by other means to avoid unpredictable
         * risks.</li>
         * </ul>
         *
         * @param name an application name
         * @return an instance
         */
        public static @NotNull OAuth2Application registerIfAbsent(@NotNull String name) {
            Objects.requireNonNull(name);
            return STORAGE.computeIfAbsent(name, OAuth2Application::new);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2ApplicationHub() {}

}

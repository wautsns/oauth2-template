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
package com.github.wautsns.oauth2template.core.basic.model.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2Platform}.
 *
 * @author wautsns
 * @see OAuth2Platform
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2PlatformHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull String, @NotNull OAuth2Platform> STORAGE =
            new ConcurrentHashMap<>();

    // ##################################################################################

    /**
     * Return an instance of the given platform name.
     *
     * @param name a platform name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2Platform find(@NotNull String name) {
        return STORAGE.get(name);
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name.
     *
     * @param name a platform name
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2Platform> optional(@NotNull String name) {
        return Optional.ofNullable(STORAGE.get(name));
    }

    /**
     * Return an instance of the given platform name.
     *
     * @param name a platform name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @NotNull OAuth2Platform acquire(@NotNull String name) {
        OAuth2Platform instance = STORAGE.get(name);
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2Platform instance of platform `%s` is registered.",
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
    public static @NotNull Stream<@NotNull OAuth2Platform> stream() {
        return STORAGE.values().stream();
    }

    // ##################################################################################

    /** The utility for manipulating the hub. */
    public static final class Manipulation {

        /**
         * Return an instance of the given platform name if exists, otherwise a new instance will be
         * registered and returned.
         *
         * @param name a platform name
         * @return an instance
         */
        public static @NotNull OAuth2Platform registerIfAbsent(@NotNull String name) {
            return STORAGE.computeIfAbsent(name, OAuth2Platform::new);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw the instance of the given platform name.
         *
         * @param name a platform name
         * @return a withdrawn instance, or {@code null} if not exists
         */
        public static @Nullable OAuth2Platform withdraw(@NotNull String name) {
            return STORAGE.remove(name);
        }

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
        public static void withdrawIf(@NotNull Predicate<OAuth2Platform> filter) {
            STORAGE.values().removeIf(filter);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2PlatformHub() {}

}

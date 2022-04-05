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

import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2Template}.
 *
 * @author wautsns
 * @see OAuth2Template
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2TemplateHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull OAuth2PlatformApplication, @NotNull OAuth2Template<?, ?, ?, ?>> STORAGE =
            new ConcurrentHashMap<>();

    // ##################################################################################

    /**
     * Return an instance of the given platform application.
     *
     * @param platformApplication a platform application
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2Template<?, ?, ?, ?> find(
            @NotNull OAuth2PlatformApplication platformApplication) {
        return STORAGE.get(platformApplication);
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform application.
     *
     * @param platformApplication a platform application
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2Template<?, ?, ?, ?>> optional(
            @NotNull OAuth2PlatformApplication platformApplication) {
        return Optional.ofNullable(STORAGE.get(platformApplication));
    }

    /**
     * Return an instance of the given platform application.
     *
     * @param platformApplication a platform application
     * @return an instance if exists, otherwise {@code null}
     */
    public static @NotNull OAuth2Template<?, ?, ?, ?> acquire(
            @NotNull OAuth2PlatformApplication platformApplication) {
        OAuth2Template<?, ?, ?, ?> instance = STORAGE.get(platformApplication);
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2Template instance of platform application `%s` is registered.",
                    platformApplication
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a sequential stream with all instances registered in the hub as its source.
     *
     * @return a sequential stream
     */
    public static @NotNull Stream<@NotNull OAuth2Template<?, ?, ?, ?>> stream() {
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
         * <li>For the same platform application, only the latest registered instance will take
         * effect.</li>
         * </ul>
         *
         * @param instance an instance
         * @return a previous instance, or {@code null} if not exist
         */
        public static @Nullable OAuth2Template<?, ?, ?, ?> register(
                @NotNull OAuth2Template<?, ?, ?, ?> instance) {
            return STORAGE.put(instance.getPlatformApplication(), instance);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw the instance of the given platform application.
         *
         * @param platformApplication a platform application
         * @return a withdrawn instance, or {@code null} if not exists
         */
        public static @Nullable OAuth2Template<?, ?, ?, ?> withdraw(
                @NotNull OAuth2PlatformApplication platformApplication) {
            return STORAGE.remove(platformApplication);
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
        public static void withdrawIf(@NotNull Predicate<OAuth2Template<?, ?, ?, ?>> filter) {
            STORAGE.values().removeIf(filter);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2TemplateHub() {}

}

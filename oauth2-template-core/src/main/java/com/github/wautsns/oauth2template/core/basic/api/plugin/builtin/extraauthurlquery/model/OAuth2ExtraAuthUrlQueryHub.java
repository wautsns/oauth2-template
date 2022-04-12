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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2ExtraAuthUrlQuery}.
 *
 * @author wautsns
 * @see OAuth2ExtraAuthUrlQuery
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ExtraAuthUrlQueryHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull StorageKey, @NotNull OAuth2ExtraAuthUrlQuery> STORAGE =
            new HashMap<>();

    // ##################################################################################

    /**
     * Return an instance of the given platform name and identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier (unique in the platform)
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2ExtraAuthUrlQuery find(
            @NotNull String platformName, @NotNull String identifier) {
        StorageKey key = new StorageKey(platformName, identifier);
        return STORAGE.get(key);
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name and
     * identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier (unique in the platform)
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2ExtraAuthUrlQuery> optional(
            @NotNull String platformName, @NotNull String identifier) {
        StorageKey key = new StorageKey(platformName, identifier);
        return Optional.ofNullable(STORAGE.get(key));
    }

    /**
     * Return an instance of the given platform name and identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier (unique in the platform)
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2ExtraAuthUrlQuery acquire(
            @NotNull String platformName, @NotNull String identifier) {
        StorageKey key = new StorageKey(platformName, identifier);
        OAuth2ExtraAuthUrlQuery instance = STORAGE.get(key);
        if (instance == null) {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2ExtraAuthUrlQuery instance of platform `%s` and identifier `%s`" +
                            " is registered.",
                    platformName, identifier
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
    public static @NotNull Stream<@NotNull OAuth2ExtraAuthUrlQuery> stream() {
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
         * <li>For the same platform name and identifier, only the latest registered instance will
         * take effect.</li>
         * </ul>
         *
         * @param instance an instance
         * @return a previous instance, or {@code null} if not exist
         */
        public static @Nullable OAuth2ExtraAuthUrlQuery register(
                @NotNull OAuth2ExtraAuthUrlQuery instance) {
            String platformName = instance.getPlatform().getName();
            String identifier = instance.getIdentifier();
            StorageKey key = new StorageKey(platformName, identifier);
            return STORAGE.put(key, instance);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw the instance of the given platform name and identifier.
         *
         * @param platformName a platform name
         * @param identifier an identifier (unique in the platform)
         * @return a withdrawn instance, or {@code null} if not exists
         */
        public static @Nullable OAuth2ExtraAuthUrlQuery withdraw(
                @NotNull String platformName, @NotNull String identifier) {
            StorageKey key = new StorageKey(platformName, identifier);
            return STORAGE.remove(key);
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
        public static void withdrawIf(@NotNull Predicate<OAuth2ExtraAuthUrlQuery> filter) {
            STORAGE.values().removeIf(filter);
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Storage key. */
    private static final class StorageKey {

        /** Platform name. */
        public final @NotNull String platformName;
        /** Identifier. */
        public final @NotNull String identifier;

        // ##################################################################################

        /**
         * Construct a new instance.
         *
         * @param platformName a platform name
         * @param identifier an identifier (unique in the platform)
         */
        public StorageKey(@NotNull String platformName, @NotNull String identifier) {
            this.platformName = Objects.requireNonNull(platformName);
            this.identifier = Objects.requireNonNull(identifier);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {return true;}
            if (obj == null || getClass() != obj.getClass()) {return false;}
            StorageKey that = (StorageKey) obj;
            if (!platformName.equals(that.platformName)) {return false;}
            return identifier.equals(that.identifier);
        }

        @Override
        public int hashCode() {
            int result = platformName.hashCode();
            result = 31 * result + identifier.hashCode();
            return result;
        }

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2ExtraAuthUrlQueryHub() {}

}

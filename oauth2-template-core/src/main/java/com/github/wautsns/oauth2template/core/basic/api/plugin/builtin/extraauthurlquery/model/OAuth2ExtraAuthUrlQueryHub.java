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
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The hub for {@link OAuth2ExtraAuthUrlQuery}.
 *
 * @author wautsns
 * @see OAuth2ExtraAuthUrlQuery
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ExtraAuthUrlQueryHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull String, @NotNull Map<@NotNull String, @NotNull OAuth2ExtraAuthUrlQuery>> STORAGE =
            new HashMap<>();
    /** Read write lock. */
    private static final @NotNull ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    // ##################################################################################

    /**
     * Return an instance of the given platform name and identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2ExtraAuthUrlQuery find(
            @NotNull String platformName, @NotNull String identifier) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            Map<String, OAuth2ExtraAuthUrlQuery> substorage = STORAGE.get(platformName);
            return (substorage == null) ? null : substorage.get(identifier);
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name and
     * identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2ExtraAuthUrlQuery> optional(
            @NotNull String platformName, @NotNull String identifier) {
        return Optional.ofNullable(find(platformName, identifier));
    }

    /**
     * Return an instance of the given platform name and identifier.
     *
     * @param platformName a platform name
     * @param identifier an identifier
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2ExtraAuthUrlQuery acquire(
            @NotNull String platformName, @NotNull String identifier) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            Map<String, OAuth2ExtraAuthUrlQuery> substorage = STORAGE.get(platformName);
            if (substorage == null) {
                throw new IllegalArgumentException(String.format(
                        "No OAuth2ExtraAuthUrlQuery instance of platform `%s` is registered.",
                        platformName
                ));
            }
            OAuth2ExtraAuthUrlQuery instance = substorage.get(identifier);
            if (instance == null) {
                throw new IllegalArgumentException(String.format(
                        "No OAuth2ExtraAuthUrlQuery instance of identifier `%s` is registered.",
                        identifier
                ));
            }
            return instance;
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Perform the given action for each instance registered in the hub.
     *
     * @param action an action to be performed
     */
    public static void forEach(@NotNull Consumer<@NotNull OAuth2ExtraAuthUrlQuery> action) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            for (Map<String, OAuth2ExtraAuthUrlQuery> substorage : STORAGE.values()) {
                substorage.values().forEach(action);
            }
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
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
            READ_WRITE_LOCK.writeLock().lock();
            try {
                String platformName = instance.getPlatform().getName();
                String identifier = instance.getIdentifier();
                return STORAGE.computeIfAbsent(platformName, key -> new HashMap<>())
                        .put(identifier, instance);
            } finally {
                READ_WRITE_LOCK.writeLock().unlock();
            }
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw the instance of the given platform name and identifier.
         *
         * @param platformName a platform name
         * @param identifier an identifier
         * @return a withdrawn instance, or {@code null} if not exists
         */
        public static @Nullable OAuth2ExtraAuthUrlQuery withdraw(
                @NotNull String platformName, @NotNull String identifier) {
            READ_WRITE_LOCK.writeLock().lock();
            try {
                Map<String, OAuth2ExtraAuthUrlQuery> substorage = STORAGE.get(platformName);
                return (substorage == null) ? null : substorage.remove(identifier);
            } finally {
                READ_WRITE_LOCK.writeLock().unlock();
            }
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
            READ_WRITE_LOCK.writeLock().lock();
            try {
                Iterator<Map<String, OAuth2ExtraAuthUrlQuery>> iterator =
                        STORAGE.values().iterator();
                while (iterator.hasNext()) {
                    Map<String, OAuth2ExtraAuthUrlQuery> substorage = iterator.next();
                    substorage.values().removeIf(filter);
                    if (substorage.isEmpty()) {
                        iterator.remove();
                    }
                }
            } finally {
                READ_WRITE_LOCK.writeLock().unlock();
            }
        }

        // ##################################################################################

        /** No need to instantiate. */
        private Manipulation() {}

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2ExtraAuthUrlQueryHub() {}

}

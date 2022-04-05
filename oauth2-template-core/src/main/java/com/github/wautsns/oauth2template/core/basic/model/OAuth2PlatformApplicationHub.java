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
package com.github.wautsns.oauth2template.core.basic.model;

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
 * The hub for {@link OAuth2PlatformApplication}.
 *
 * @author wautsns
 * @see OAuth2PlatformApplication
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2PlatformApplicationHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull String, @NotNull Map<@NotNull String, @NotNull OAuth2PlatformApplication>> STORAGE =
            new HashMap<>();
    /** Read write lock. */
    private static final @NotNull ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    // ##################################################################################

    /**
     * Return an instance of the given platform name and application name.
     *
     * @param platformName a platform name
     * @param applicationName an application name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2PlatformApplication find(
            @NotNull String platformName, @NotNull String applicationName) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            Map<String, OAuth2PlatformApplication> substorage = STORAGE.get(platformName);
            return (substorage == null) ? null : substorage.get(applicationName);
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name and application
     * name.
     *
     * @param platformName a platform name
     * @param applicationName an application name
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2PlatformApplication> optional(
            @NotNull String platformName, @NotNull String applicationName) {
        return Optional.ofNullable(find(platformName, applicationName));
    }

    /**
     * Return an instance of the given platform name and application name.
     *
     * @param platformName a platform name
     * @param applicationName an application name
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2PlatformApplication acquire(
            @NotNull String platformName, @NotNull String applicationName) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            Map<String, OAuth2PlatformApplication> substorage = STORAGE.get(platformName);
            if (substorage == null) {
                throw new IllegalArgumentException(String.format(
                        "No OAuth2PlatformApplication instance of platform `%s` is registered.",
                        platformName
                ));
            }
            OAuth2PlatformApplication instance = substorage.get(applicationName);
            if (instance == null) {
                throw new IllegalArgumentException(String.format(
                        "No OAuth2PlatformApplication instance of application `%s` is registered.",
                        applicationName
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
    public static void forEach(@NotNull Consumer<@NotNull OAuth2PlatformApplication> action) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            for (Map<String, OAuth2PlatformApplication> substorage : STORAGE.values()) {
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
         * <li>For the same platform name and application name, only the latest registered instance
         * will take effect.</li>
         * </ul>
         *
         * @param instance an instance
         * @return a previous instance, or {@code null} if not exist
         */
        public static @Nullable OAuth2PlatformApplication register(
                @NotNull OAuth2PlatformApplication instance) {
            READ_WRITE_LOCK.writeLock().lock();
            try {
                String platformName = instance.getPlatform().getName();
                String applicationName = instance.getApplication().getName();
                return STORAGE.computeIfAbsent(platformName, key -> new HashMap<>())
                        .put(applicationName, instance);
            } finally {
                READ_WRITE_LOCK.writeLock().unlock();
            }
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Withdraw the instance of the given platform name and application name.
         *
         * @param platformName a platform name
         * @param applicationName an application name
         * @return a withdrawn instance, or {@code null} if not exists
         */
        public static @Nullable OAuth2PlatformApplication withdraw(
                @NotNull String platformName, @NotNull String applicationName) {
            READ_WRITE_LOCK.writeLock().lock();
            try {
                Map<String, OAuth2PlatformApplication> substorage = STORAGE.get(platformName);
                return (substorage == null) ? null : substorage.remove(applicationName);
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
        public static void withdrawIf(@NotNull Predicate<OAuth2PlatformApplication> filter) {
            READ_WRITE_LOCK.writeLock().lock();
            try {
                Iterator<Map<String, OAuth2PlatformApplication>> iterator =
                        STORAGE.values().iterator();
                while (iterator.hasNext()) {
                    Map<String, OAuth2PlatformApplication> substorage = iterator.next();
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
    private OAuth2PlatformApplicationHub() {}

}

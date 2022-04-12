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

import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2PlatformApplication}.
 *
 * @author wautsns
 * @see OAuth2PlatformApplication
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2PlatformApplicationHub {

    /** Data storage. */
    private static final @NotNull Map<@NotNull StorageKey, @NotNull OAuth2PlatformApplication> STORAGE =
            new ConcurrentHashMap<>();

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
        StorageKey key = new StorageKey(platformName, applicationName);
        return STORAGE.get(key);
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
        StorageKey key = new StorageKey(platformName, applicationName);
        return Optional.ofNullable(STORAGE.get(key));
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
        StorageKey key = new StorageKey(platformName, applicationName);
        OAuth2PlatformApplication instance = STORAGE.get(key);
        if (instance == null) {
            throw new IllegalArgumentException(String.format(
                    "No OAuth2PlatformApplication instance of platform `%s` and application `%s`" +
                            " is registered.",
                    platformName, applicationName
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
    public static @NotNull Stream<@NotNull OAuth2PlatformApplication> stream() {
        return STORAGE.values().stream();
    }

    // ##################################################################################

    /** The utility for manipulating the hub. */
    public static final class Manipulation {

        /**
         * Return an instance of the given platform name if exists, otherwise a new instance will be
         * registered and returned.
         *
         * <ul>
         * <li style="list-style-type:none">########## Notes ###############</li>
         * <li>There is currently no direct method to remove registered instances, and it is
         * <em>highly not recommended</em> to remove them by other means to avoid unpredictable
         * risks.</li>
         * </ul>
         *
         * @param platformName a platform name
         * @param applicationName an application name
         * @return an instance
         */
        public static @NotNull OAuth2PlatformApplication registerIfAbsent(
                @NotNull String platformName, @NotNull String applicationName) {
            StorageKey key = new StorageKey(platformName, applicationName);
            return STORAGE.computeIfAbsent(key, sk -> {
                return OAuth2ApiFactoryHub.acquire(sk.platformName)
                        .initializePlatformApplication(sk.applicationName);
            });
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
        /** Application name. */
        public final @NotNull String applicationName;

        // ##################################################################################

        /**
         * Construct a new instance.
         *
         * @param platformName a platform name
         * @param applicationName an application name
         */
        public StorageKey(@NotNull String platformName, @NotNull String applicationName) {
            this.platformName = Objects.requireNonNull(platformName);
            this.applicationName = Objects.requireNonNull(applicationName);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {return true;}
            if (obj == null || getClass() != obj.getClass()) {return false;}
            StorageKey that = (StorageKey) obj;
            if (!platformName.equals(that.platformName)) {return false;}
            return applicationName.equals(that.applicationName);
        }

        @Override
        public int hashCode() {
            int result = platformName.hashCode();
            result = 31 * result + applicationName.hashCode();
            return result;
        }

    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2PlatformApplicationHub() {}

}

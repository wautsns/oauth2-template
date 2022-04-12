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

import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactoryHub;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The hub for {@link OAuth2Platform} based on {@link OAuth2ApiFactoryHub}.
 *
 * @author wautsns
 * @see OAuth2Platform
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2PlatformHub {

    /**
     * Return an instance of the given platform name.
     *
     * @param name a platform name
     * @return an instance if exists, otherwise {@code null}
     */
    public static @Nullable OAuth2Platform find(@NotNull String name) {
        return optional(name).orElse(null);
    }

    /**
     * Return an {@code Optional} describing the instance of the given platform name.
     *
     * @param name a platform name
     * @return an {@code Optional} with the instance, otherwise an empty {@code Optional}
     */
    public static @NotNull Optional<OAuth2Platform> optional(@NotNull String name) {
        return OAuth2ApiFactoryHub.optional(name).map(OAuth2ApiFactory::getPlatform);
    }

    /**
     * Return an instance of the given platform name.
     *
     * @param name a platform name
     * @return an instance
     * @throws IllegalArgumentException if there is no instance available
     */
    public static @NotNull OAuth2Platform acquire(@NotNull String name) {
        OAuth2ApiFactory<?, ?, ?, ?> apiFactory = OAuth2ApiFactoryHub.find(name);
        if (apiFactory != null) {
            return apiFactory.getPlatform();
        } else {
            throw new IllegalArgumentException(String.format(
                    "No `%s`-related OAuth2ApiFactory instance is registered.",
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
        return OAuth2ApiFactoryHub.stream().map(OAuth2ApiFactory::getPlatform);
    }

    // ##################################################################################

    /** No need to instantiate. */
    private OAuth2PlatformHub() {}

}

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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2UrlQuery;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Abstract OAuth2 extra auth url query.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2ExtraAuthUrlQuery {

    /** Identifier (unique in the platform). */
    private final @NotNull String identifier;

    // ##################################################################################

    /**
     * Return the platform this instance belongs to.
     *
     * @return the platform this instance belongs to
     */
    public abstract @NotNull OAuth2Platform getPlatform();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Set extra auth url query to the given query.
     *
     * @param query a query
     */
    public abstract void encodeAndSetToQuery(@NotNull OAuth2UrlQuery query);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given identifier.
     *
     * @param identifier an identifier (unique in the platform)
     */
    protected OAuth2ExtraAuthUrlQuery(@NotNull String identifier) {
        this.identifier = Objects.requireNonNull(identifier);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final @NotNull String getIdentifier() {
        return identifier;
    }

}

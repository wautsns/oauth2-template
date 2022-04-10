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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general;

import com.github.wautsns.oauth2template.core.utility.http.utility.OAuth2HttpMultiValueMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 http headers.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2HttpHeaders extends OAuth2HttpMultiValueMap<OAuth2HttpHeaders> {

    /**
     * Associate the given value with the name {@code "Accept"}.
     *
     * @param value a value to be associated with the name {@code "Accept"}
     * @return self reference
     * @see #set(String, String)
     */
    public @NotNull OAuth2HttpHeaders setAccept(@Nullable String value) {
        return set("Accept", value);
    }

    /**
     * Associate the value {@code "application/json"} with the name {@code "Accept"}.
     *
     * @return self reference
     * @see #set(String, String)
     */
    public @NotNull OAuth2HttpHeaders setAcceptJson() {
        return set("Accept", "application/json");
    }

    /**
     * Associate the given value with the name {@code "Authorization"}.
     *
     * @param value a value to be associated with the name {@code "Authorization"}
     * @return self reference
     * @see #set(String, String)
     */
    public @NotNull OAuth2HttpHeaders setAuthorization(@Nullable String value) {
        return set("Authorization", value);
    }

    /**
     * Associate the built-in value with the name {@code "User-Agent"}.
     *
     * @return self reference
     * @see #set(String, String)
     */
    public @NotNull OAuth2HttpHeaders setUserAgentBuiltin() {
        String value = "Mozilla/5.0 (X11; Linux x86_64) MyWebKit/98.6.27 OAuth2Template/98.2.5";
        return set("User-Agent", value);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull OAuth2HttpHeaders copy() {
        return new OAuth2HttpHeaders(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull String encode(@NotNull String text) {
        return text;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given initial capacity.
     *
     * @param initialCapacity initial capacity, a recommended value is the maximum number of
     *         different names that can be set (for OAuth2, the value will not be very large)
     */
    public OAuth2HttpHeaders(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @see #copy()
     */
    private OAuth2HttpHeaders(@NotNull OAuth2HttpHeaders template) {
        super(template);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}

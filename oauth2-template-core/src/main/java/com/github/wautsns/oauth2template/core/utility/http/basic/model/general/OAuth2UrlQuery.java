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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 URL query.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2UrlQuery extends OAuth2HttpMultiValueMap<OAuth2UrlQuery> {

    @Override
    public @NotNull OAuth2UrlQuery copy() {
        return new OAuth2UrlQuery(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given initial capacity.
     *
     * @param initialCapacity initial capacity, a recommended value is the maximum number of
     *         different names that can be set (for OAuth2, the value will not be very large)
     */
    public OAuth2UrlQuery(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @see #copy()
     */
    private OAuth2UrlQuery(@NotNull OAuth2UrlQuery template) {
        super(template);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a query string.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li><em>(empty string)</em></li>
     * <li>?nameA=value&amp;nameB=valueA&amp;nameB=valueB</li>
     * </ul>
     *
     * @return a query string, or empty string if there is no query item
     */
    @Override
    public @NotNull String toString() {
        if (isEmpty()) {return "";}
        StringBuilder builder = new StringBuilder();
        builder.append('?');
        forEach((key, value) -> builder.append(key).append('=').append(value).append('&'));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull String encode(@NotNull String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
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

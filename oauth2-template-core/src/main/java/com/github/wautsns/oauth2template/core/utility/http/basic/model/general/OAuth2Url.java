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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * OAuth2 URL.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2Url {

    /** URL without query. */
    private final @NotNull String urlWithoutQuery;
    /** URL query. */
    private final @NotNull OAuth2UrlQuery query;
    /** URL anchor. */
    private @Nullable String anchor;

    // ##################################################################################

    /**
     * Create a new instance by deep copying this instance.
     *
     * @return a copy of this instance
     */
    public @NotNull OAuth2Url copy() {
        return new OAuth2Url(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given URL and query initial capacity.
     *
     * @param urlWithoutQuery a URL without query
     * @param queryInitialCapacity query initial capacity, a recommended value is the
     *         maximum number of different names that can be set (for OAuth2, the value will not be
     *         very large)
     * @see OAuth2UrlQuery#OAuth2UrlQuery(int)
     */
    public OAuth2Url(@NotNull String urlWithoutQuery, int queryInitialCapacity) {
        this.urlWithoutQuery = urlWithoutQuery;
        this.query = new OAuth2UrlQuery(queryInitialCapacity);
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @see #copy()
     */
    private OAuth2Url(@NotNull OAuth2Url template) {
        this.urlWithoutQuery = template.urlWithoutQuery;
        this.query = template.query.copy();
        this.anchor = template.anchor;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a URL string.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>https://github.com</li>
     * <li>https://github.com?nameA=value&amp;nameB=valueA&amp;nameB=valueB</li>
     * <li>https://github.com?name=value#anchor</li>
     * </ul>
     *
     * @return a URL string
     */
    @Override
    public @NotNull String toString() {
        String url = urlWithoutQuery + query;
        return (anchor == null) ? url : (url + "#" + anchor);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull String getUrlWithoutQuery() {
        return urlWithoutQuery;
    }

    public @NotNull OAuth2UrlQuery getQuery() {
        return query;
    }

    public @Nullable String getAnchor() {
        return anchor;
    }

    public @NotNull OAuth2Url setAnchor(@Nullable String anchor) {
        this.anchor = anchor;
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        OAuth2Url that = (OAuth2Url) obj;
        if (!urlWithoutQuery.equals(that.urlWithoutQuery)) {return false;}
        if (!query.equals(that.query)) {return false;}
        return Objects.equals(anchor, that.anchor);
    }

    @Override
    public int hashCode() {
        int result = urlWithoutQuery.hashCode();
        result = 31 * result + query.hashCode();
        result = 31 * result + Objects.hashCode(anchor);
        return result;
    }

}

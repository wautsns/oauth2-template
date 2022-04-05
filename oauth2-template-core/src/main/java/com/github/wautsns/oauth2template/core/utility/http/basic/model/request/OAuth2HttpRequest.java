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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.request;

import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 http request.
 *
 * @param <E> the type of http entity
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2HttpRequest<E extends OAuth2HttpEntity<E>> {

    /** Http method. */
    private final @NotNull OAuth2HttpMethod method;
    /** Target URL. */
    private final @NotNull OAuth2Url url;
    /** Http headers. */
    private @Nullable OAuth2HttpHeaders headers;
    /** Http entity. */
    private @Nullable E entity;

    // ##################################################################################

    /**
     * Create a new instance by deep copying this instance.
     *
     * @param shareUrl whether to share url
     * @param shareHeaders whether to share headers
     * @param shareEntity whether to share entity
     * @return a copy of this instance
     */
    public @NotNull OAuth2HttpRequest<E> copy(
            boolean shareUrl, boolean shareHeaders, boolean shareEntity) {
        return new OAuth2HttpRequest<>(this, shareUrl, shareHeaders, shareEntity);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given http method and URL.
     *
     * @param method http method
     * @param url target URL
     * @see OAuth2HttpMethod#request(String, int)
     */
    public OAuth2HttpRequest(@NotNull OAuth2HttpMethod method, @NotNull OAuth2Url url) {
        this.method = method;
        this.url = url;
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @param shareUrl whether to share url
     * @param shareHeaders whether to share headers
     * @param shareEntity whether to share entity
     * @see #copy(boolean, boolean, boolean)
     */
    private OAuth2HttpRequest(
            @NotNull OAuth2HttpRequest<E> template, boolean shareUrl, boolean shareHeaders,
            boolean shareEntity) {
        this.method = template.method;
        this.url = shareUrl ? template.url : template.url.copy();
        if (template.headers != null) {
            this.headers = shareHeaders ? template.headers : template.headers.copy();
        }
        if (template.entity != null) {
            this.entity = shareEntity ? template.entity : template.entity.copy();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a string describing this instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>GET https://github.com/</li>
     * <li>GET https://github.com/ Headers:{headerA:value,headerB:[valueA,valueB]}</li>
     * <li>GET https://github.com/ Headers:{headerA:value} Entity:name=value</li>
     * </ul>
     *
     * @return a descriptive string
     */
    @Override
    public @NotNull String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append(method).append(' ').append(url);
        if (headers != null) {
            temp.append(" Headers:").append(headers);
        }
        if (entity != null) {
            temp.append(" Entity:").append(entity);
        }
        return temp.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull OAuth2HttpMethod getMethod() {
        return method;
    }

    public @NotNull OAuth2Url getUrl() {
        return url;
    }

    public @Nullable OAuth2HttpHeaders getHeaders() {
        return headers;
    }

    public @NotNull OAuth2HttpRequest<E> setHeaders(@Nullable OAuth2HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public @Nullable E getEntity() {
        return entity;
    }

    public @NotNull OAuth2HttpRequest<E> setEntity(@Nullable E entity) {
        this.entity = entity;
        return this;
    }

}

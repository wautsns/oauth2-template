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

import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;

import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of OAuth2 http method.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public enum OAuth2HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    // ##################################################################################

    /**
     * Create a new http request with the given URL and query initial capacity.
     *
     * @param urlWithoutQuery a URL without query
     * @param queryInitialCapacity query initial capacity, a recommended value is the
     *         maximum number of different names that can be set (for OAuth2, the value will not be
     *         very large)
     * @param <E> the type of http entity
     * @return a new http request
     */
    public <E extends OAuth2HttpEntity<E>> @NotNull OAuth2HttpRequest<E> request(
            @NotNull String urlWithoutQuery, int queryInitialCapacity) {
        return new OAuth2HttpRequest<>(this, new OAuth2Url(urlWithoutQuery, queryInitialCapacity));
    }

}

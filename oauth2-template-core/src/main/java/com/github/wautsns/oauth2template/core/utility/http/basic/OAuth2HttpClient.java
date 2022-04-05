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
package com.github.wautsns.oauth2template.core.utility.http.basic;

import com.github.wautsns.oauth2template.core.exception.OAuth2IOException;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Abstract OAuth2 http client.
 *
 * @author wautsns
 * @see OAuth2HttpClientFactory
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2HttpClient {

    /** Logger. */
    private static final @NotNull Logger log = LoggerFactory.getLogger(OAuth2HttpClient.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Empty headers. */
    private static final @NotNull OAuth2HttpHeaders EMPTY_HEADERS = new OAuth2HttpHeaders(0);

    // ##################################################################################

    /**
     * Execute the given http request.
     *
     * @param request a http request
     * @return a http response
     * @throws OAuth2IOException if an I/O error occurs
     */
    public final @NotNull OAuth2HttpResponse execute(@NotNull OAuth2HttpRequest<?> request)
            throws OAuth2IOException {
        OAuth2HttpMethod method = request.getMethod();
        String url = request.getUrl().toString();
        OAuth2HttpHeaders headers = request.getHeaders();
        headers = (headers == null) ? EMPTY_HEADERS : headers;
        OAuth2HttpEntity<?> entity = request.getEntity();
        try {
            log.debug("Ready to execute request. request:{}", request);
            OAuth2HttpResponse response = execute(method, url, headers, entity);
            log.debug(
                    "Request has been executed. statusCode:{},responseBody:{}",
                    response.getStatusCode(), response.parseBodyAsString()
            );
            return response;
        } catch (IOException e) {
            log.error("Failed to execute request. request:{}", request, e);
            throw new OAuth2IOException(e);
        } catch (RuntimeException e) {
            log.error("Failed to execute request. request:{}", request, e);
            throw e;
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given properties.
     *
     * @param properties http client properties
     * @throws IllegalArgumentException if the implementation in the properties is not
     *         assignable from this class
     */
    protected OAuth2HttpClient(@NotNull OAuth2HttpClientProperties properties) {
        if (!properties.getImplementation().isAssignableFrom(getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The implementation `%s` in OAuth2HttpClientProperties is not assignable" +
                            " from `%s`.",
                    properties.getImplementation(), getClass()
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Initialize a http request and execute it.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>All params will be treated as unmodifiable.</li>
     * </ul>
     *
     * @param method http method
     * @param url target URL
     * @param headers http headers
     * @param entity http entity
     * @return http response
     * @throws IOException if an I/O error occurs
     * @implSpec <ul>
     *         <li>All params should be treated as unmodifiable.</li>
     *         <li>Add headers through <code><i>headers</i>.{@link
     *         OAuth2HttpHeaders#forEach(BiConsumer) forEach(<i>action</i>)}</code>.</li>
     *         <li>If the entity is not {@code null}, add it through {@link
     *         OAuth2HttpEntity#getContentType()} and {@link OAuth2HttpEntity#getBodyString()}.</li>
     *         </ul>
     */
    protected abstract @NotNull OAuth2HttpResponse execute(
            @NotNull OAuth2HttpMethod method, @NotNull String url,
            @NotNull OAuth2HttpHeaders headers, @Nullable OAuth2HttpEntity<?> entity)
            throws IOException;

}

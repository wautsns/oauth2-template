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
package com.github.wautsns.oauth2template.extension.http.httpcomponents.model;

import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * OAuth2 http response based on apache httpcomponents.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class HttpcomponentsOAuth2HttpResponse extends OAuth2HttpResponse {

    /** A {@link HttpResponse} instance used as delegate. */
    private final @NotNull HttpResponse delegate;

    // ##################################################################################

    @Override
    public int getStatusCode() {
        return delegate.getStatusLine().getStatusCode();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @Nullable String getHeader(@NotNull String name) {
        Objects.requireNonNull(name);
        Header header = delegate.getFirstHeader(name);
        return (header == null) ? null : header.getValue();
    }

    @Override
    public @NotNull List<@NotNull String> getHeaders(@NotNull String name) {
        Objects.requireNonNull(name);
        return Arrays.stream(delegate.getHeaders(name))
                .map(Header::getValue)
                .collect(Collectors.toList());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected @Nullable InputStream getBodyInputStream() throws IOException {
        HttpEntity entity = delegate.getEntity();
        return (entity == null) ? null : entity.getContent();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param delegate a {@link HttpResponse} instance used as delegate
     */
    public HttpcomponentsOAuth2HttpResponse(@NotNull HttpResponse delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

}

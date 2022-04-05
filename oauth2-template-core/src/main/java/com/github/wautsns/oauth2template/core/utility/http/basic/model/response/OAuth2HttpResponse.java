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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.response;

import com.github.wautsns.oauth2template.core.exception.OAuth2IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Abstract OAuth2 http response.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2HttpResponse {

    /** JSON utility. */
    private static final @NotNull ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // ##################################################################################

    /** Whether the body input stream is resolved. */
    private boolean resolved;
    /**
     * The body string.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Only valid when {@link #resolved} is {@code true}.</li>
     * </ul>
     */
    private @Nullable String resolvedBodyString;

    // ##################################################################################

    /**
     * Return http status code.
     *
     * @return http status code
     */
    public abstract int getStatusCode();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the value associated with the given header name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there are multiple values associated with the header name, only the first one will be
     * returned.</li>
     * </ul>
     *
     * @param name a header name
     * @return the value associated with the header name, or {@code null} if not exist
     */
    public abstract @Nullable String getHeader(@NotNull String name);

    /**
     * Return all values associated with the given header name.
     *
     * @param name a header name
     * @return all values associated with the header name
     */
    public abstract @NotNull List<@NotNull String> getHeaders(@NotNull String name);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Parse body as string.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The body input stream will only be read once.</li>
     * </ul>
     *
     * @return a body string, or {@code null} if there is no input stream
     * @throws OAuth2IOException if an I/O error occurs
     */
    public final synchronized @Nullable String parseBodyAsString() throws OAuth2IOException {
        if (resolved) {return resolvedBodyString;}
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                InputStream inputStream = getBodyInputStream()) {
            if (inputStream == null) {
                resolvedBodyString = null;
            } else {
                byte[] buffer = new byte[512];
                for (int length; (length = inputStream.read(buffer)) != -1; ) {
                    outputStream.write(buffer, 0, length);
                }
                resolvedBodyString = outputStream.toString(StandardCharsets.UTF_8.name());
            }
            resolved = true;
            return resolvedBodyString;
        } catch (IOException e) {
            throw new OAuth2IOException(e);
        }
    }

    /**
     * Parse body as JSON.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The body input stream will only be read once.</li>
     * </ul>
     *
     * @return a root node of JSON, or {@link NullNode} if there is no input stream
     * @throws OAuth2IOException if an I/O error occurs
     */
    public final @NotNull JsonNode parseBodyAsJson() throws OAuth2IOException {
        String bodyString = parseBodyAsString();
        if (bodyString == null) {
            return NullNode.getInstance();
        } else {
            try {
                return OBJECT_MAPPER.readTree(bodyString);
            } catch (JsonProcessingException e) {
                throw new OAuth2IOException(e);
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a body input stream.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The body input stream will only be read once.</li>
     * </ul>
     *
     * @return a body input stream, or {@code null} if there is none
     * @throws IOException if an I/O error occurs
     */
    protected abstract @Nullable InputStream getBodyInputStream() throws IOException;

}

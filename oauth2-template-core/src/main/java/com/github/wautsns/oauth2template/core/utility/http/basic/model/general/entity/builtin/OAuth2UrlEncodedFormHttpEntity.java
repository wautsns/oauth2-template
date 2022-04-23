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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.builtin;

import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.utility.OAuth2HttpMultiValueMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

/**
 * OAuth2 http entity for Content-Type: {@code application/x-www-form-urlencoded}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2UrlEncodedFormHttpEntity
        extends OAuth2HttpEntity<OAuth2UrlEncodedFormHttpEntity> {

    /** Data storage. */
    private final @NotNull Storage storage;

    // ##################################################################################

    @Override
    public @NotNull String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}, or empty string if there is no form item
     */
    @Override
    public @NotNull String getBodyString() {
        if (storage.isEmpty()) {return "";}
        StringBuilder builder = new StringBuilder();
        storage.forEach((name, value) -> {
            builder.append(name).append('=').append(value).append('&');
        });
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return whether this instance contains no mappings.
     *
     * @return {@code true} if it does, otherwise {@code false}
     */
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the value associated with the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there are multiple values associated with the name, only the first one will be
     * returned.</li>
     * </ul>
     *
     * @param name a name
     * @return the value associated with the name, or {@code null} if not exist
     */
    public @Nullable String get(@NotNull String name) {
        return storage.get(name);
    }

    /**
     * Perform the given action for each name and every value associated with.
     *
     * @param action an action to be performed
     */
    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull String> action) {
        storage.forEach(action);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Associate the given value with the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@code null}, an invocation of this method is equivalent to <code>{@link
     * #remove(String) this.remove}(<i>name</i>)</code>.</li>
     * <li>If the name has associated with other values, all of them will be lost.</li>
     * </ul>
     *
     * @param name a name with which the value is to be associated
     * @param value a value to be associated with the name
     * @return self reference
     * @see #encodeAndSet(String, String)
     * @see #add(String, String)
     */
    public @NotNull OAuth2UrlEncodedFormHttpEntity set(
            @NotNull String name, @Nullable String value) {
        storage.set(name, value);
        return this;
    }

    /**
     * Encode the given text (call it <em>value</em>), and then associate the <em>value</em> with
     * the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@code null}, an invocation of this method is equivalent to <code>{@link
     * #remove(String) this.remove}(<i>name</i>)</code>.</li>
     * <li>If the name has associated with other values, all of them will be lost.</li>
     * </ul>
     *
     * @param name a name with which the <em>value</em> is to be associated
     * @param text a text to be encoded as <em>value</em> to be associated with the name
     * @return self reference
     * @see #set(String, String)
     * @see #encodeAndAdd(String, String)
     */
    public @NotNull OAuth2UrlEncodedFormHttpEntity encodeAndSet(
            @NotNull String name, @Nullable String text) {
        storage.encodeAndSet(name, text);
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Associate the given value with the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@code null}, nothing will be done.</li>
     * <li>If the name has associated with other values, the value will be added to the end.</li>
     * </ul>
     *
     * @param name a name with which the value is to be associated
     * @param value a value to be associated with the name
     * @return self reference
     * @see #encodeAndAdd(String, String)
     * @see #set(String, String)
     */
    public @NotNull OAuth2UrlEncodedFormHttpEntity add(
            @NotNull String name, @Nullable String value) {
        storage.add(name, value);
        return this;
    }

    /**
     * Encode the given text (call it <em>value</em>), and then associate the <em>value</em> with
     * the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the text is {@code null}, nothing will be done.</li>
     * <li>If the name has associated with other values, the <em>value</em> will be added to the
     * end.</li>
     * </ul>
     *
     * @param name a name with which the <em>value</em> is to be associated
     * @param text a text to be encoded as <em>value</em> to be associated with the name
     * @return self reference
     * @see #add(String, String)
     * @see #encodeAndSet(String, String)
     */
    public @NotNull OAuth2UrlEncodedFormHttpEntity encodeAndAdd(
            @NotNull String name, @Nullable String text) {
        storage.encodeAndAdd(name, text);
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Remove the mapping between the given name and its associated values.
     *
     * @param name the name of the mapping to be removed
     * @return self reference
     */
    public @NotNull OAuth2UrlEncodedFormHttpEntity remove(@NotNull String name) {
        storage.remove(name);
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull OAuth2UrlEncodedFormHttpEntity copy() {
        return new OAuth2UrlEncodedFormHttpEntity(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given initial capacity.
     *
     * @param initialCapacity initial capacity, a recommended value is the maximum number of
     *         different names that can be set (for OAuth2, the value will not be very large)
     */
    public OAuth2UrlEncodedFormHttpEntity(int initialCapacity) {
        this.storage = new Storage(initialCapacity);
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @see #copy()
     */
    private OAuth2UrlEncodedFormHttpEntity(@NotNull OAuth2UrlEncodedFormHttpEntity template) {
        this.storage = template.storage.copy();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        OAuth2UrlEncodedFormHttpEntity that = (OAuth2UrlEncodedFormHttpEntity) obj;
        return storage.equals(that.storage);
    }

    @Override
    public int hashCode() {
        return storage.hashCode();
    }

    // ##################################################################################

    /** Storage for saving URL form items. */
    private static final class Storage extends OAuth2HttpMultiValueMap<Storage> {

        @Override
        public @NotNull Storage copy() {
            return new Storage(this);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Construct a new instance with the given initial capacity.
         *
         * @param initialCapacity initial capacity, a recommended value is the maximum
         *         number of different names that can be set (for OAuth2, the value will not be very
         *         large)
         */
        public Storage(int initialCapacity) {
            super(initialCapacity);
        }

        /**
         * Construct a new instance by deep copying the given template.
         *
         * @param template a template for deep copy
         * @see #copy()
         */
        private Storage(@NotNull Storage template) {
            super(template);
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

    }

}

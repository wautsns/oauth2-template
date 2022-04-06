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
package com.github.wautsns.oauth2template.core.utility.http.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Abstract OAuth2 http multi value map.
 *
 * @param <S> the type of implementation class
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@SuppressWarnings("unchecked")
public abstract class OAuth2HttpMultiValueMap<S extends OAuth2HttpMultiValueMap<S>> {

    /** Initial capacity. */
    private final int initialCapacity;
    /**
     * Data storage.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Types of map values may be {@code String} or {@code LinkedList&lt;String&gt;}. The
     * reason for using this data structure is because it is relatively rare for a name to
     * correspond to multiple values for OAuth2.</li>
     * </ul>
     */
    private final @NotNull Map<@NotNull String, @NotNull Object> storage;

    // ##################################################################################

    /**
     * Return whether this instance contains no mappings.
     *
     * @return {@code true} if it does, otherwise {@code false}
     */
    public final boolean isEmpty() {
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
    public final @Nullable String get(@NotNull String name) {
        Object multiValue = storage.get(name);
        if (multiValue == null) {
            return null;
        } else if (multiValue instanceof String) {
            return (String) multiValue;
        } else {
            return ((LinkedList<String>) multiValue).getFirst();
        }
    }

    /**
     * Perform the given action for each name and every value associated with.
     *
     * @param action an action to be performed
     */
    public final void forEach(@NotNull BiConsumer<@NotNull String, @NotNull String> action) {
        for (Map.Entry<String, Object> entry : storage.entrySet()) {
            String name = entry.getKey();
            Object multiValue = entry.getValue();
            if (multiValue instanceof String) {
                action.accept(name, (String) multiValue);
            } else {
                for (String value : (LinkedList<String>) multiValue) {
                    action.accept(name, value);
                }
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Encode the given text.
     *
     * @param text a text to be encoded
     * @return the encoded value, or the original text if no need to encode
     */
    public abstract @NotNull String encode(@NotNull String text);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Associate the given value with the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@code null}, an invocation of this method is equivalent to <code>{@link
     * #remove(String) this.remove}(<em>name</em>)</code>.</li>
     * <li>If the name has associated with other values, all of them will be lost.</li>
     * </ul>
     *
     * @param name a name with which the value is to be associated
     * @param value a value to be associated with the name
     * @return self reference
     * @see #encodeAndSet(String, String)
     * @see #add(String, String)
     */
    public final @NotNull S set(@NotNull String name, @Nullable String value) {
        if (value == null) {
            remove(name);
        } else {
            storage.put(name, value);
        }
        return (S) this;
    }

    /**
     * Encode the given text (call it <em>value</em>), and then associate the <em>value</em> with
     * the given name.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@code null}, an invocation of this method is equivalent to <code>{@link
     * #remove(String) this.remove}(<em>name</em>)</code>.</li>
     * <li>If the name has associated with other values, all of them will be lost.</li>
     * </ul>
     *
     * @param name a name with which the <em>value</em> is to be associated
     * @param text a text to be encoded as <em>value</em> to be associated with the name
     * @return self reference
     * @see #set(String, String)
     * @see #encodeAndAdd(String, String)
     */
    public final @NotNull S encodeAndSet(@NotNull String name, @Nullable String text) {
        if (text == null) {
            remove(name);
        } else {
            storage.put(name, encode(text));
        }
        return (S) this;
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
    public final @NotNull S add(@NotNull String name, @Nullable String value) {
        if (value == null) {return (S) this;}
        Object multiValue = storage.get(name);
        if (multiValue == null) {
            storage.put(name, value);
        } else if (multiValue instanceof String) {
            LinkedList<String> values = new LinkedList<>();
            values.addLast((String) multiValue);
            values.addLast(value);
            storage.put(name, values);
        } else {
            ((LinkedList<String>) multiValue).add(value);
        }
        return (S) this;
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
    public final @NotNull S encodeAndAdd(@NotNull String name, @Nullable String text) {
        return (text == null) ? (S) this : add(name, encode(text));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Remove the mapping between the given name and its associated values.
     *
     * @param name the name of the mapping to be removed
     * @return self reference
     */
    public final @NotNull S remove(@NotNull String name) {
        storage.remove(name);
        return (S) this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a new instance by deep copying this instance.
     *
     * @return a copy of this instance
     */
    public abstract @NotNull S copy();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given initial capacity.
     *
     * @param initialCapacity initial capacity, a recommended value is the maximum number of
     *         different names that can be set (for OAuth2, the value will not be very large)
     */
    protected OAuth2HttpMultiValueMap(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        this.storage = new HashMap<>(initialCapacity, 1F);
    }

    /**
     * Construct a new instance by deep copying the given template.
     *
     * @param template a template for deep copy
     * @see #copy()
     */
    protected OAuth2HttpMultiValueMap(@NotNull S template) {
        this(((OAuth2HttpMultiValueMap<S>) template).initialCapacity);
        template.forEach(this::add);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a string describing relationship between name and its associated values.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>{nameA=value, nameB=[valueA, valueB]}</li>
     * </ul>
     *
     * @return a descriptive string
     */
    @Override
    public @NotNull String toString() {
        return storage.toString();
    }

}

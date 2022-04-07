/*
 * Copyright 2022 the original author or authors.
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
package com.github.wautsns.oauth2template.core.utility.ctx;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An OAuth2 context.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2Context extends AbstractMap<Object, Object> {

    /**
     * Create a new OAuth2 context, using {@link HashMap} as delegate (initial capacity: 4).
     *
     * @return a new OAuth2 context
     * @see #delegate(Map)
     */
    public static @NotNull OAuth2Context hashMap() {
        return delegate(new HashMap<>(4));
    }

    /**
     * Create a new OAuth2 context, using {@link ConcurrentHashMap} as delegate.
     *
     * @return a new OAuth2 context
     * @see #delegate(Map)
     */
    public static @NotNull OAuth2Context concurrentHashMap() {
        return delegate(new ConcurrentHashMap<>());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Create a new OAuth2 context with the given delegate.
     *
     * @param delegate a {@link Map} instance used as delegate
     * @return a new OAuth2 context
     */
    public static @NotNull OAuth2Context delegate(@NotNull Map<Object, Object> delegate) {
        return new OAuth2Context(delegate);
    }

    // ##################################################################################

    /** A {@link Map} instance used as delegate. */
    private final @NotNull Map<Object, Object> delegate;

    // ##################################################################################

    @Override
    public @NotNull Set<@NotNull Entry<Object, Object>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public Object put(Object key, Object value) {
        return delegate.put(key, value);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given delegate.
     *
     * @param delegate a {@link Map} instance used as delegate
     */
    private OAuth2Context(@NotNull Map<Object, Object> delegate) {
        this.delegate = delegate;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        OAuth2Context that = (OAuth2Context) obj;
        return delegate == that.delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

}

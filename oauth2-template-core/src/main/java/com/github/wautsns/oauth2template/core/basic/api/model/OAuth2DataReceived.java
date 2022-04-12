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
package com.github.wautsns.oauth2template.core.basic.api.model;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * OAuth2 data received from platform.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2DataReceived {

    /** Raw data received from platform. */
    protected final @NotNull JsonNode rawData;
    /** Timestamp when received. */
    protected final long timestamp;
    /** Thread-unsafe OAuth2 context. */
    protected final @NotNull OAuth2Context context;

    // ##################################################################################

    /**
     * Return the platform this instance belongs to.
     *
     * @return the platform this instance belongs to
     */
    public abstract @NotNull OAuth2Platform getPlatform();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the value associated with the given name.
     *
     * @param name a name
     * @return the value associated with the name, or {@code null} if the json node is {@link
     *         MissingNode} or {@link NullNode}
     */
    public final @Nullable Boolean getBoolean(@NotNull String name) {
        return getValue(name, JsonNode::asBoolean);
    }

    /**
     * Return the value associated with the given name.
     *
     * @param name a name
     * @return the value associated with the name, or {@code null} if the json node is {@link
     *         MissingNode} or {@link NullNode}
     */
    public final @Nullable Long getLong(@NotNull String name) {
        return getValue(name, JsonNode::asLong);
    }

    /**
     * Return the value associated with the given name.
     *
     * @param name a name
     * @return the value associated with the name, or {@code null} if the json node is {@link
     *         MissingNode} or {@link NullNode}
     */
    public final @Nullable String getString(@NotNull String name) {
        return getValue(name, JsonNode::asText);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the value associated with the given name.
     *
     * @param name a name
     * @param convertor a convertor for the json node associated with the name
     * @param <T> the type of value to be returned
     * @return the value associated with the name, or {@code null} if the json node is {@link
     *         MissingNode} or {@link NullNode} or the convertor returned {@code null}
     */
    protected final <T> @Nullable T getValue(
            @NotNull String name, @NotNull Function<@NotNull JsonNode, @Nullable T> convertor) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(convertor);
        JsonNode node = rawData.path(name);
        return (node.isMissingNode() || node.isNull()) ? null : convertor.apply(node);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance, using current timestamp.
     *
     * @param rawData a raw data received from platform
     * @see #OAuth2DataReceived(JsonNode, long)
     */
    protected OAuth2DataReceived(@NotNull JsonNode rawData) {
        this(rawData, System.currentTimeMillis());
    }

    /**
     * Construct a new instance, using {@link OAuth2Context#hashMap()} as context.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @see #OAuth2DataReceived(JsonNode, long, OAuth2Context)
     */
    protected OAuth2DataReceived(@NotNull JsonNode rawData, long timestamp) {
        this(rawData, timestamp, OAuth2Context.hashMap());
    }

    /**
     * Construct a new instance.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @param context an OAuth2 context
     */
    protected OAuth2DataReceived(
            @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
        this.rawData = Objects.requireNonNull(rawData);
        this.timestamp = timestamp;
        this.context = Objects.requireNonNull(context);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a string describing this instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>[github|1648968526736] rawData:`name=value`,context:`{name=value}`</li>
     * </ul>
     *
     * @return a descriptive string
     */
    @Override
    public final String toString() {
        return String.format(
                "[%s|%s] rawData:%s,context:%s",
                getPlatform().getName(), timestamp, rawData, context
        );
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final @NotNull JsonNode getRawData() {
        return rawData;
    }

    public final long getTimestamp() {
        return timestamp;
    }

    public final @NotNull OAuth2Context getContext() {
        return context;
    }

}

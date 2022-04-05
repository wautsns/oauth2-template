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
package com.github.wautsns.oauth2template.core.basic.api.model.callback;

import com.github.wautsns.oauth2template.core.basic.api.model.OAuth2DataReceived;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract OAuth2 auth callback url query.
 *
 * @author wautsns
 * @implSpec Typically, an OAuth2 auth callback url query includes <em>code</em> and
 *         <em>state</em>. For ease of use, several abstract getters related to them are required
 *         to be implemented. Names of other fields may vary greatly from platform to platform. For
 *         ease of understanding, all of them are defined by the implementation class.
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2Callback extends OAuth2DataReceived {

    /**
     * Return authorization code.
     *
     * @return authorization code, or {@code null} if not exist
     */
    public abstract @Nullable String getCode();

    /**
     * Return state.
     *
     * @return state, or {@code null} if not exist
     */
    public abstract @Nullable String getState();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance, using current timestamp.
     *
     * @param rawData a raw data received from platform
     * @see #OAuth2Callback(JsonNode, long)
     */
    protected OAuth2Callback(@NotNull JsonNode rawData) {
        super(rawData);
    }

    /**
     * Construct a new instance, using {@link OAuth2Context#hashMap()} as context.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @see #OAuth2Callback(JsonNode, long, OAuth2Context)
     */
    protected OAuth2Callback(@NotNull JsonNode rawData, long timestamp) {
        super(rawData, timestamp);
    }

    /**
     * Construct a new instance.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @param context an OAuth2 context
     */
    protected OAuth2Callback(
            @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
        super(rawData, timestamp, context);
    }

}

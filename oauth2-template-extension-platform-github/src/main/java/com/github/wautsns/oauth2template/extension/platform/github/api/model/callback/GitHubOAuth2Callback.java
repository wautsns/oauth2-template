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
package com.github.wautsns.oauth2template.extension.platform.github.api.model.callback;

import com.github.wautsns.oauth2template.core.basic.api.model.callback.OAuth2Callback;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GitHub OAuth2 auth callback url query.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2Callback extends OAuth2Callback {

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return GitHubOAuth2.PLATFORM;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @Nullable String getCode() {
        return getString("code");
    }

    @Override
    public @Nullable String getState() {
        return getString("state");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance, using current timestamp.
     *
     * @param rawData a raw data received from platform
     * @see #GitHubOAuth2Callback(JsonNode, long)
     */
    public GitHubOAuth2Callback(@NotNull JsonNode rawData) {
        super(rawData);
    }

    /**
     * Construct a new instance, using {@link OAuth2Context#hashMap()} as context.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @see #GitHubOAuth2Callback(JsonNode, long, OAuth2Context)
     */
    public GitHubOAuth2Callback(@NotNull JsonNode rawData, long timestamp) {
        super(rawData, timestamp);
    }

    /**
     * Construct a new instance.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @param context an OAuth2 context
     */
    public GitHubOAuth2Callback(
            @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
        super(rawData, timestamp, context);
    }

}

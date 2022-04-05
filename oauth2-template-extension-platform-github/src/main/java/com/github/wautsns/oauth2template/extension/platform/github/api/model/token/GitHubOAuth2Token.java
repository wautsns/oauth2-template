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
package com.github.wautsns.oauth2template.extension.platform.github.api.model.token;

import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * GitHub OAuth2 token.
 *
 * <ul>
 * <li style="list-style-type:none">########## Examples ###############</li>
 * <li style="list-style-type:none"><pre>
 * {
 *     "access_token":"gho_HbLlwO3H0maskPo9x99H0869mask9999mask",
 *     "token_type":"bearer",
 *     "scope":"notifications,user:email"
 * }
 * </pre></li>
 * </ul>
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2Token extends OAuth2Token {

    /** TODO Not tested, temporarily assumed to be 1 day. */
    private static final @NotNull Duration ACCESS_TOKEN_VALIDITY_DURATION = Duration.ofDays(1);

    // ##################################################################################

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return GitHubOAuth2.PLATFORM;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull String getAccessToken() {
        return rawData.required("access_token").asText();
    }

    @Override
    public @NotNull Duration getAccessTokenValidityDuration() {
        return ACCESS_TOKEN_VALIDITY_DURATION;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance, using current timestamp.
     *
     * @param rawData a raw data received from platform
     * @see #GitHubOAuth2Token(JsonNode, long)
     */
    public GitHubOAuth2Token(@NotNull JsonNode rawData) {
        super(rawData);
    }

    /**
     * Construct a new instance, using {@link OAuth2Context#hashMap()} as context.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @see #GitHubOAuth2Token(JsonNode, long, OAuth2Context)
     */
    public GitHubOAuth2Token(@NotNull JsonNode rawData, long timestamp) {
        super(rawData, timestamp);
    }

    /**
     * Construct a new instance.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @param context an OAuth2 context
     */
    public GitHubOAuth2Token(
            @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
        super(rawData, timestamp, context);
    }

}

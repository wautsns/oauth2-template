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
package com.github.wautsns.oauth2template.extension.platform.github.model;

import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * GitHub OAuth2 platform application.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2PlatformApplication extends OAuth2PlatformApplication {

    /** Client id received from github for oauth app. */
    private String clientId;
    /** Client secret received from github for oauth app. */
    private String clientSecret;
    /** Authorization callback url received from github for oauth app. */
    private String authorizationCallbackUrl;

    // ##################################################################################

    @Override
    public void validate() {
        Objects.requireNonNull(clientId);
        Objects.requireNonNull(clientSecret);
        Objects.requireNonNull(authorizationCallbackUrl);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param applicationName an application name
     */
    public GitHubOAuth2PlatformApplication(@NotNull String applicationName) {
        super(GitHubOAuth2.PLATFORM_NAME, applicationName);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getClientId() {
        return clientId;
    }

    public @NotNull GitHubOAuth2PlatformApplication setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public @NotNull GitHubOAuth2PlatformApplication setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getAuthorizationCallbackUrl() {
        return authorizationCallbackUrl;
    }

    public @NotNull GitHubOAuth2PlatformApplication setAuthorizationCallbackUrl(
            String authorizationCallbackUrl) {
        this.authorizationCallbackUrl = authorizationCallbackUrl;
        return this;
    }

}

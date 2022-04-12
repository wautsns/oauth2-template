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
package com.github.wautsns.oauth2template.extension.platform.github.template;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.OAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.template.OAuth2Template;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.callback.GitHubOAuth2Callback;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.token.GitHubOAuth2Token;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.user.GitHubOAuth2User;
import com.github.wautsns.oauth2template.extension.platform.github.model.GitHubOAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * GitHub OAuth2 template.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2Template
        extends OAuth2Template<GitHubOAuth2PlatformApplication, GitHubOAuth2Callback, GitHubOAuth2Token, GitHubOAuth2User> {

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     */
    public GitHubOAuth2Template(@NotNull GitHubOAuth2PlatformApplication platformApplication) {
        super(platformApplication);
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     */
    public GitHubOAuth2Template(
            @NotNull GitHubOAuth2PlatformApplication platformApplication,
            @NotNull OAuth2HttpClient httpClient) {
        super(platformApplication, httpClient);
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param plugins plugins
     */
    public GitHubOAuth2Template(
            @NotNull GitHubOAuth2PlatformApplication platformApplication,
            @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        super(platformApplication, plugins);
    }

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param httpClient a http client
     * @param plugins plugins
     */
    public GitHubOAuth2Template(
            @NotNull GitHubOAuth2PlatformApplication platformApplication,
            @NotNull OAuth2HttpClient httpClient, @NotNull List<@NotNull OAuth2ApiPlugin> plugins) {
        super(platformApplication, httpClient, plugins);
    }

}

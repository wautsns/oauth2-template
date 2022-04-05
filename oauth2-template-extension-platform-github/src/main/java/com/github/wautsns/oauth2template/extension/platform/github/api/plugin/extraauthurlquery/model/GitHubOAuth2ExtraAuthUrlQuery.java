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
package com.github.wautsns.oauth2template.extension.platform.github.api.plugin.extraauthurlquery.model;

import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQuery;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2UrlQuery;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;
import com.github.wautsns.oauth2template.extension.platform.github.model.GitHubOAuth2Scope;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GitHub OAuth2 auth url query properties.
 *
 * @author wautsns
 * @see <a href="https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps">
 *         Authorizing OAuth Apps - GitHub Docs</a>
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2ExtraAuthUrlQuery extends OAuth2ExtraAuthUrlQuery {

    /** Suggests a specific account to use for signing in and authorizing the app. */
    private String login;
    /**
     * A space-delimited list of
     * <a href="/en/apps/building-oauth-apps/understanding-scopes-for-oauth-apps">scopes</a>.
     * If not provided, <code>scope</code> defaults to an empty list for users that have not
     * authorized any scopes for the application. For users who have authorized scopes for the
     * application, the user won't be shown the OAuth authorization page with the list of scopes.
     * Instead, this step of the flow will automatically extra with the set of scopes the user has
     * authorized for the application. For example, if a user has already performed the web flow
     * twice and has authorized one token with <code>user</code> scope and another token with
     * <code>repo</code> scope, a third web flow that does not provide a <code>scope</code> will
     * receive a token with <code>user</code> and <code>repo</code> scope.
     */
    private List<@NotNull GitHubOAuth2Scope> scopes;
    /**
     * Whether unauthenticated users will be offered an option to sign up for GitHub during the
     * OAuth flow. The default is <code>true</code>. Use <code>false</code> when a policy prohibits
     * signups.
     */
    private Boolean allowSignup;

    // ##################################################################################

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return GitHubOAuth2.PLATFORM;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void encodeAndSetToQuery(@NotNull OAuth2UrlQuery query) {
        if (login == null) {
            query.remove("login");
        } else {
            query.encodeAndSet("login", login);
        }
        if (scopes == null) {
            query.remove("scope");
        } else {
            String scope = scopes.stream()
                    .map(GitHubOAuth2Scope::getValue)
                    .collect(Collectors.joining(" "));
            query.encodeAndSet("scope", scope);
        }
        if (allowSignup == null) {
            query.remove("allowSignup");
        } else {
            query.encodeAndSet("allow_signup", allowSignup.toString());
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance with the given identifier.
     *
     * @param identifier an identifier (unique in the platform)
     */
    public GitHubOAuth2ExtraAuthUrlQuery(@NotNull String identifier) {
        super(identifier);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getLogin() {
        return login;
    }

    public @NotNull GitHubOAuth2ExtraAuthUrlQuery setLogin(String login) {
        this.login = login;
        return this;
    }

    public List<@NotNull GitHubOAuth2Scope> getScopes() {
        return scopes;
    }

    public @NotNull GitHubOAuth2ExtraAuthUrlQuery setScopes(
            List<@NotNull GitHubOAuth2Scope> scopes) {
        this.scopes = scopes;
        return this;
    }

    public Boolean getAllowSignup() {
        return allowSignup;
    }

    public @NotNull GitHubOAuth2ExtraAuthUrlQuery setAllowSignup(Boolean allowSignup) {
        this.allowSignup = allowSignup;
        return this;
    }

}

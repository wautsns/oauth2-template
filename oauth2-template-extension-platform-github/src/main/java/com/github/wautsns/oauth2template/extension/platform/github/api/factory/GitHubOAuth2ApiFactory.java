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
package com.github.wautsns.oauth2template.extension.platform.github.api.factory;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.factory.OAuth2ApiFactory;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.exception.specific.OAuth2AccessTokenExpiredException;
import com.github.wautsns.oauth2template.core.exception.specific.OAuth2UserDeniedAuthorizationException;
import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.GitHubOAuth2DataReceived;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.callback.GitHubOAuth2Callback;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.token.GitHubOAuth2Token;
import com.github.wautsns.oauth2template.extension.platform.github.api.model.user.GitHubOAuth2User;
import com.github.wautsns.oauth2template.extension.platform.github.model.GitHubOAuth2PlatformApplication;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * GitHub OAuth2Api factory.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2ApiFactory
        extends OAuth2ApiFactory<GitHubOAuth2PlatformApplication, GitHubOAuth2Callback, GitHubOAuth2Token, GitHubOAuth2User> {

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return GitHubOAuth2.PLATFORM;
    }

    @Override
    public @NotNull GitHubOAuth2PlatformApplication initializePlatformApplication(
            @NotNull String applicationName) {
        return new GitHubOAuth2PlatformApplication(applicationName);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull GitHubOAuth2Callback initializeCallback(@NotNull JsonNode rawData) {
        return new GitHubOAuth2Callback(rawData);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull OAuth2ApiBuildAuthUrl createApiBuildAuthUrl(
            @NotNull GitHubOAuth2PlatformApplication platformApplication) {
        String urlWithoutQuery = "https://github.com/login/oauth/authorize";
        OAuth2Url template = new OAuth2Url(urlWithoutQuery, 5);
        template.getQuery()
                .encodeAndSet("client_id", platformApplication.getClientId())
                .encodeAndSet("redirect_uri", platformApplication.getAuthorizationCallbackUrl());
        return context -> template.copy();
    }

    @Override
    public @NotNull OAuth2ApiExchTokenWithCallback<GitHubOAuth2Callback, GitHubOAuth2Token> createApiExchTokenWithCallback(
            @NotNull GitHubOAuth2PlatformApplication platformApplication,
            @NotNull OAuth2HttpClient httpClient) {
        String urlWithoutQuery = "https://github.com/login/oauth/access_token";
        OAuth2HttpRequest<?> template = OAuth2HttpMethod.GET.request(urlWithoutQuery, 3);
        template.getUrl().getQuery()
                .encodeAndSet("client_id", platformApplication.getClientId())
                .encodeAndSet("client_secret", platformApplication.getClientSecret());
        template.setHeaders(new OAuth2HttpHeaders(1).setAcceptJson());
        return query -> {
            String code = query.getCode();
            if (code == null) {
                String error = query.getString("error");
                if ("access_denied".equals(error)) {
                    throw new OAuth2UserDeniedAuthorizationException(query.toString());
                } else {
                    throw new OAuth2Exception(query.toString());
                }
            } else {
                OAuth2HttpRequest<?> request = template.copy(false, true, true);
                request.getUrl().getQuery().encodeAndSet("code", code);
                OAuth2HttpResponse response = httpClient.execute(request);
                JsonNode rawData = response.parseBodyAsJson();
                String error = rawData.path("error").asText(null);
                if (error == null) {
                    return new GitHubOAuth2Token(rawData);
                } else {
                    throw new OAuth2Exception(new GitHubOAuth2DataReceived(rawData).toString());
                }
            }
        };
    }

    @Override
    public @NotNull OAuth2ApiExchUserWithToken<GitHubOAuth2Token, GitHubOAuth2User> createApiExchUserWithToken(
            @NotNull GitHubOAuth2PlatformApplication platformApplication,
            @NotNull OAuth2HttpClient httpClient) {
        String urlWithoutQuery = "https://api.github.com/user";
        OAuth2HttpRequest<?> template = OAuth2HttpMethod.GET.request(urlWithoutQuery, 0);
        return token -> {
            OAuth2HttpRequest<?> request = template.copy(true, true, true);
            OAuth2HttpHeaders headers = new OAuth2HttpHeaders(1)
                    .setAuthorization("token " + token.getAccessToken());
            request.setHeaders(headers);
            OAuth2HttpResponse response = httpClient.execute(request);
            JsonNode rawData = response.parseBodyAsJson();
            if (response.getStatusCode() < 300) {
                return new GitHubOAuth2User(rawData);
            } else {
                GitHubOAuth2DataReceived data = new GitHubOAuth2DataReceived(rawData);
                if ("Bad credentials".equals(data.getString("message"))) {
                    throw new OAuth2AccessTokenExpiredException("Bad credentials", data);
                } else {
                    throw new OAuth2Exception(data.toString());
                }
            }
        };
    }

}

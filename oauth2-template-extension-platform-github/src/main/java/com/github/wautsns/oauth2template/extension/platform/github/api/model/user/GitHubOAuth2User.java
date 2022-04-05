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
package com.github.wautsns.oauth2template.extension.platform.github.api.model.user;

import com.github.wautsns.oauth2template.core.basic.api.model.user.OAuth2User;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.extension.platform.github.GitHubOAuth2;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * GitHub OAuth2 user.
 *
 * <ul>
 * <li style="list-style-type:none">########## Examples ###############</li>
 * <li style="list-style-type:none"><pre>
 * {
 *     "login":"wautsns",
 *     "id":39336604,
 *     "node_id":"MDQ6VXNlcjM5MzM2NjA0",
 *     "avatar_url":"https://avatars.githubusercontent.com/u/39336604?v=4",
 *     "gravatar_id":"",
 *     "url":"https://api.github.com/users/wautsns",
 *     "html_url":"https://github.com/wautsns",
 *     "followers_url":"https://api.github.com/users/wautsns/followers",
 *     "following_url":"https://api.github.com/users/wautsns/following{/other_user}",
 *     "gists_url":"https://api.github.com/users/wautsns/gists{/gist_id}",
 *     "starred_url":"https://api.github.com/users/wautsns/starred{/owner}{/repo}",
 *     "subscriptions_url":"https://api.github.com/users/wautsns/subscriptions",
 *     "organizations_url":"https://api.github.com/users/wautsns/orgs",
 *     "repos_url":"https://api.github.com/users/wautsns/repos",
 *     "events_url":"https://api.github.com/users/wautsns/events{/privacy}",
 *     "received_events_url":"https://api.github.com/users/wautsns/received_events",
 *     "type":"User",
 *     "site_admin":false,
 *     "name":"wautsns",
 *     "company":null,
 *     "blog":"",
 *     "location":null,
 *     "email":null,
 *     "hireable":null,
 *     "bio":null,
 *     "twitter_username":null,
 *     "public_repos":7,
 *     "public_gists":0,
 *     "followers":0,
 *     "following":0,
 *     "created_at":"2018-05-16T12:17:46Z",
 *     "updated_at":"2022-04-04T14:16:27Z"
 * }
 * </pre></li>
 * </ul>
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2User extends OAuth2User {

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return GitHubOAuth2.PLATFORM;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull String getIdentifier() {
        return rawData.required("id").asText();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance, using current timestamp.
     *
     * @param rawData a raw data received from platform
     * @see #GitHubOAuth2User(JsonNode, long)
     */
    public GitHubOAuth2User(@NotNull JsonNode rawData) {
        super(rawData);
    }

    /**
     * Construct a new instance, using {@link OAuth2Context#hashMap()} as context.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @see #GitHubOAuth2User(JsonNode, long, OAuth2Context)
     */
    public GitHubOAuth2User(@NotNull JsonNode rawData, long timestamp) {
        super(rawData, timestamp);
    }

    /**
     * Construct a new instance.
     *
     * @param rawData a raw data received from platform
     * @param timestamp timestamp when received
     * @param context an OAuth2 context
     */
    public GitHubOAuth2User(
            @NotNull JsonNode rawData, long timestamp, @NotNull OAuth2Context context) {
        super(rawData, timestamp, context);
    }

}

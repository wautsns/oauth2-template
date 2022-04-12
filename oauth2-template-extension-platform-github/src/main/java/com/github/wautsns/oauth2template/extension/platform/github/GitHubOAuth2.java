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
package com.github.wautsns.oauth2template.extension.platform.github;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;

import org.jetbrains.annotations.NotNull;

/**
 * GitHub OAuth2.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class GitHubOAuth2 {

    /** GitHub OAuth2 platform name. */
    public static final @NotNull String PLATFORM_NAME = "github";
    /** GitHub OAuth2 platform. */
    public static final @NotNull OAuth2Platform PLATFORM = new OAuth2Platform(PLATFORM_NAME);

    // ##################################################################################

    /** No need to instantiate. */
    private GitHubOAuth2() {}

}

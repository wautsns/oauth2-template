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
package com.github.wautsns.oauth2template.extension.http.httpcomponents;

import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactory;

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 http client factory based on apache httpcomponents.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class HttpcomponentsOAuth2HttpClientFactory extends OAuth2HttpClientFactory {

    @Override
    public @NotNull Class<HttpcomponentsOAuth2HttpClient> getTargetClass() {
        return HttpcomponentsOAuth2HttpClient.class;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull HttpcomponentsOAuth2HttpClient create(
            @NotNull OAuth2HttpClientProperties properties) {
        return new HttpcomponentsOAuth2HttpClient(properties);
    }

}

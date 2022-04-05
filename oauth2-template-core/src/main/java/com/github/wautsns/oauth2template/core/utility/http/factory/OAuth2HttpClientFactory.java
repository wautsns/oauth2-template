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
package com.github.wautsns.oauth2template.core.utility.http.factory;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract OAuth2 http client factory.
 *
 * @author wautsns
 * @see OAuth2HttpClientFactoryHub
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2HttpClientFactory {

    /**
     * Return the target {@link OAuth2HttpClient} class.
     *
     * @return the target class
     */
    public abstract @NotNull Class<? extends OAuth2HttpClient> getTargetClass();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Create a new http client with the given properties.
     *
     * @param properties http client properties
     * @return a new http client
     */
    public abstract @NotNull OAuth2HttpClient create(
            @NotNull OAuth2HttpClientProperties properties);

}

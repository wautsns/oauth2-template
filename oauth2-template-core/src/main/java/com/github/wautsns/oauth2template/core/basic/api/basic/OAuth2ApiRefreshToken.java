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
package com.github.wautsns.oauth2template.core.basic.api.basic;

import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract OAuth2Api: <em>RefreshToken</em>.
 *
 * @param <T> the type of token
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@FunctionalInterface
public interface OAuth2ApiRefreshToken<T extends OAuth2Token> extends OAuth2Api<T, T> {

    /**
     * Refresh the given token.
     *
     * @param oldToken an old token
     * @return a new token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    @Override
    @NotNull T execute(@NotNull T oldToken) throws OAuth2Exception;

}

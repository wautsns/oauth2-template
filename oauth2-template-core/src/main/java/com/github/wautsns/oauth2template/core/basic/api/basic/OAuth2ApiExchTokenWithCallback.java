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

import com.github.wautsns.oauth2template.core.basic.api.model.callback.OAuth2Callback;
import com.github.wautsns.oauth2template.core.basic.api.model.token.OAuth2Token;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract OAuth2Api: <em>ExchTokenWithCallback</em>.
 *
 * @param <C> the type of auth callback URL query
 * @param <T> the type of token
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@FunctionalInterface
public interface OAuth2ApiExchTokenWithCallback<C extends OAuth2Callback, T extends OAuth2Token>
        extends OAuth2Api<C, T> {

    /**
     * Exchange a token with the given callback.
     *
     * @param callback an auth callback URL query
     * @return a token
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    @Override
    @NotNull T execute(@NotNull C callback) throws OAuth2Exception;

}

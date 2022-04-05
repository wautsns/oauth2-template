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
package com.github.wautsns.oauth2template.core.basic.api.basic;

import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.UnknownNullability;

/**
 * Abstract OAuth2Api.
 *
 * @param <I> the type of input
 * @param <R> the type of result
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@FunctionalInterface
public interface OAuth2Api<I, R> {

    /**
     * Execute this OAuth2Api with the given input.
     *
     * @param input an input
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    @UnknownNullability R execute(@UnknownNullability I input) throws OAuth2Exception;

}

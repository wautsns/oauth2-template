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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract OAuth2 http entity.
 *
 * @param <S> the type of implementation class
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2HttpEntity<S extends OAuth2HttpEntity<S>> {

    /**
     * Return the content type.
     *
     * @return the content type
     */
    public abstract @NotNull String getContentType();

    /**
     * Return a body string.
     *
     * @return a body string
     */
    public abstract @NotNull String getBodyString();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Create a new instance by deep copying this instance.
     *
     * @return a copy of this instance
     */
    public abstract @NotNull S copy();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a string describing this instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>[application/x-www-form-urlencoded]:nameA=value&nameB=valueA&nameB=valueB</li>
     * </ul>
     *
     * @return a descriptive string
     */
    @Override
    public final @NotNull String toString() {
        return String.format("[%s]:%s", getContentType(), getBodyString());
    }

}

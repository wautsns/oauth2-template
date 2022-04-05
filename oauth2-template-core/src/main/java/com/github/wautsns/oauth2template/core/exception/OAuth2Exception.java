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
package com.github.wautsns.oauth2template.core.exception;

import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 exception.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public class OAuth2Exception extends Exception {

    private static final long serialVersionUID = -5300684909446266523L;

    // ##################################################################################

    /**
     * Construct a new instance.
     *
     * @param message the detail message which is saved for later retrieval by the {@link
     *         #getMessage()} method
     */
    public OAuth2Exception(@Nullable String message) {
        super(message);
    }

    /**
     * Construct a new instance.
     *
     * @param cause the cause which is saved for later retrieval by the {@link #getCause()}
     *         method
     */
    public OAuth2Exception(@Nullable Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new instance.
     *
     * @param cause the cause which is saved for later retrieval by the {@link #getCause()}
     *         method
     * @param message the detail message which is saved for later retrieval by the {@link
     *         #getMessage()} method
     */
    public OAuth2Exception(@Nullable Throwable cause, @Nullable String message) {
        super(message, cause);
    }

}

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
package com.github.wautsns.oauth2template.core.exception.specific;

import com.github.wautsns.oauth2template.core.basic.api.model.OAuth2DataReceived;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 refresh token expired exception.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>This exception will be thrown when platform returns an error that refresh token has
 * expired.</li>
 * </ul>
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2RefreshTokenExpiredException extends OAuth2Exception {

    private static final long serialVersionUID = -4998755841341820049L;

    // ##################################################################################

    /** An OAuth2 data received from platform. */
    private final transient @NotNull OAuth2DataReceived dataReceived;

    // ##################################################################################

    /**
     * Construct a new instance.
     *
     * @param dataReceived an OAuth2 data received from platform
     */
    public OAuth2RefreshTokenExpiredException(@NotNull OAuth2DataReceived dataReceived) {
        super(dataReceived.toString());
        this.dataReceived = dataReceived;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull OAuth2DataReceived getDataReceived() {
        return dataReceived;
    }

}

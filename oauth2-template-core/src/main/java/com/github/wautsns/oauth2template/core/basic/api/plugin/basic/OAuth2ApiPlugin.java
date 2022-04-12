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
package com.github.wautsns.oauth2template.core.basic.api.plugin.basic;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Abstract OAuth2Api plugin.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2ApiPlugin {

    /** Interceptors. */
    protected final @NotNull List<@NotNull OAuth2ApiInterceptor> interceptors;

    // ##################################################################################

    /**
     * Return whether the plugin applies to the given params.
     *
     * @param platformApplication a platform application
     * @param isApiRefreshTokenSupported whether the platform supports api
     *         <em>RefreshToken</em>
     * @param isApiRevokeAuthSupported whether the platform supports api
     *         <em>RevokeAuth</em>
     * @return {@code true} if it does, otherwise {@code false}
     */
    public abstract boolean isApplicable(
            @NotNull OAuth2PlatformApplication platformApplication,
            boolean isApiRefreshTokenSupported, boolean isApiRevokeAuthSupported);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param interceptors interceptors
     */
    protected OAuth2ApiPlugin(@NotNull List<@NotNull OAuth2ApiInterceptor> interceptors) {
        this.interceptors = Objects.requireNonNull(interceptors);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return interceptors.
     *
     * @return interceptors
     */
    public final @NotNull List<@NotNull OAuth2ApiInterceptor> getInterceptors() {
        return interceptors;
    }

}

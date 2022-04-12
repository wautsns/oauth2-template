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
package com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Objects;

/**
 * Abstract OAuth2Api interceptor.
 *
 * @author wautsns
 * @see OAuth2ApiInterceptPoint
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2ApiInterceptor {

    /** Intercept points. */
    protected final @NotNull List<@NotNull OAuth2ApiInterceptPoint> interceptPoints;

    // ##################################################################################

    /**
     * Invoke the given invocation.
     *
     * @param invocation an invocation
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public abstract @UnknownNullability Object invoke(@NotNull OAuth2ApiInvocation invocation)
            throws OAuth2Exception;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param interceptPoints intercept points
     */
    protected OAuth2ApiInterceptor(
            @NotNull List<@NotNull OAuth2ApiInterceptPoint> interceptPoints) {
        this.interceptPoints = Objects.requireNonNull(interceptPoints);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return intercept points.
     *
     * @return intercept points
     */
    public final @NotNull List<@NotNull OAuth2ApiInterceptPoint> getInterceptPoints() {
        return interceptPoints;
    }

}

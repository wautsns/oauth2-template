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
package com.github.wautsns.oauth2template.extension.http.httpcomponents.plugin;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * Connection keep alive strategy based on apache httpcomponents.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class HttpcomponentsConnectionKeepAliveStrategy
        extends DefaultConnectionKeepAliveStrategy {

    /** Default keep alive timeout millis. */
    private final long defaultKeepAliveTimeoutMillis;

    // ##################################################################################

    @Override
    public long getKeepAliveDuration(
            @NotNull HttpResponse response, @Nullable HttpContext context) {
        long value = super.getKeepAliveDuration(response, context);
        return (value < 0) ? defaultKeepAliveTimeoutMillis : value;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param defaultKeepAliveTimeoutMillis default keep alive timeout millis
     */
    public HttpcomponentsConnectionKeepAliveStrategy(
            @NotNull Duration defaultKeepAliveTimeoutMillis) {
        this.defaultKeepAliveTimeoutMillis = defaultKeepAliveTimeoutMillis.toMillis();
    }

}

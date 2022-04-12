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
package com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;
import java.util.Objects;

/**
 * OAuth2Api invocation.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class OAuth2ApiInvocation {

    /** Platform application. */
    private final @NotNull OAuth2PlatformApplication platformApplication;
    /** OAuth2Api. */
    private final @NotNull OAuth2Api api;

    /** An interceptor iterator. */
    private final @NotNull Iterator<@NotNull OAuth2ApiInterceptor> interceptorIterator;
    /** An input. */
    private @UnknownNullability Object input;
    /** Thread-unsafe OAuth2 context. */
    private final @NotNull OAuth2Context context;

    // ##################################################################################

    /**
     * Proceed this invocation.
     *
     * @return a result of execution
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    public @UnknownNullability Object proceed() throws OAuth2Exception {
        if (interceptorIterator.hasNext()) {
            return interceptorIterator.next().invoke(this);
        } else {
            return api.execute(input);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param platformApplication a platform application
     * @param api an OAuth2Api
     * @param interceptorIterator an interceptor iterator
     * @param input an input
     */
    public OAuth2ApiInvocation(
            @NotNull OAuth2PlatformApplication platformApplication,
            @NotNull OAuth2Api<?, ?> api,
            @NotNull Iterator<@NotNull OAuth2ApiInterceptor> interceptorIterator,
            @UnknownNullability Object input) {
        this.platformApplication = Objects.requireNonNull(platformApplication);
        this.api = Objects.requireNonNull(api);
        this.interceptorIterator = Objects.requireNonNull(interceptorIterator);
        this.input = input;
        this.context = OAuth2Context.hashMap();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull OAuth2PlatformApplication getPlatformApplication() {
        return platformApplication;
    }

    public @NotNull OAuth2Api<?, ?> getApi() {
        return api;
    }

    public @UnknownNullability Object getInput() {
        return input;
    }

    public @NotNull OAuth2ApiInvocation setInput(@UnknownNullability Object input) {
        this.input = input;
        return this;
    }

    public @NotNull OAuth2Context getContext() {
        return context;
    }

}

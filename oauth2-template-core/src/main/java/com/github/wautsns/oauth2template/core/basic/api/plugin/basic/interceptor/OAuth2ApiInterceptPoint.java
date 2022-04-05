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

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2Api intercept point.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ApiInterceptPoint {

    /** The class representing the target OAuth2Api to be intercepted. */
    private final @NotNull Class<? extends OAuth2Api<?, ?>> target;
    /** An int value representing the order in which the interceptor is executed. */
    private final int order;

    // ##################################################################################

    /**
     * Construct a new instance
     *
     * @param target a class representing the target OAuth2Api to be intercepted
     * @param order an int value representing the order in which the interceptor is
     *         executed
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public OAuth2ApiInterceptPoint(@NotNull Class<? extends OAuth2Api> target, int order) {
        this.target = (Class) target;
        this.order = order;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull Class<? extends OAuth2Api<?, ?>> getTarget() {
        return target;
    }

    public int getOrder() {
        return order;
    }

}

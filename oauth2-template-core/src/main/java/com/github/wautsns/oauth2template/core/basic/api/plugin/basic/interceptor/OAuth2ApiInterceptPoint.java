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
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchTokenWithCallback;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiExchUserWithToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRefreshToken;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiRevokeAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * OAuth2Api intercept point.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ApiInterceptPoint {

    /**
     * Optional targets.
     *
     * <ul>
     * <li style="list-style-type:none">########## Optional Targets ###############</li>
     * <li>{@link OAuth2ApiBuildAuthUrl}</li>
     * <li>{@link OAuth2ApiExchTokenWithCallback}</li>
     * <li>{@link OAuth2ApiExchUserWithToken}</li>
     * <li>{@link OAuth2ApiRefreshToken}</li>
     * <li>{@link OAuth2ApiRevokeAuth}</li>
     * </ul>
     */
    private static final @NotNull Set<Class<?>> OPTIONAL_TARGETS =
            new HashSet<>(Arrays.asList(
                    OAuth2ApiBuildAuthUrl.class,
                    OAuth2ApiExchTokenWithCallback.class,
                    OAuth2ApiExchUserWithToken.class,
                    OAuth2ApiRefreshToken.class,
                    OAuth2ApiRevokeAuth.class
            ));

    // ##################################################################################

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
        this.target = (Class) Objects.requireNonNull(target);
        this.order = order;
        if (!OPTIONAL_TARGETS.contains(target)) {
            throw new IllegalArgumentException(String.format(
                    "Target `%s` is not supported. optionalTargets:%s",
                    target, OPTIONAL_TARGETS
            ));
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull Class<? extends OAuth2Api<?, ?>> getTarget() {
        return target;
    }

    public int getOrder() {
        return order;
    }

}

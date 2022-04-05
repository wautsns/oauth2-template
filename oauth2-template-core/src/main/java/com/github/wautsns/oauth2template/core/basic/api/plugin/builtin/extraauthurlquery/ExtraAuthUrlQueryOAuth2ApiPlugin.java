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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.OAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.interceptor.ExtraAuthUrlQueryOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * OAuth2Api plugin for <em>ExtraAuthUrlQuery</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class ExtraAuthUrlQueryOAuth2ApiPlugin extends OAuth2ApiPlugin {

    /** OAuth2 context variable: identifier. */
    private static final @NotNull String VARIABLE_IDENTIFIER = String.format(
            "%s_identifier",
            ExtraAuthUrlQueryOAuth2ApiPlugin.class
    );

    /**
     * Associate the given value with variable: identifier.
     *
     * @param context an OAuth2 context
     * @param identifier a variable value associated with the identifier
     */
    public static void setVariableIdentifier(
            @NotNull OAuth2Context context, @NotNull String identifier) {
        context.put(VARIABLE_IDENTIFIER, identifier);
    }

    /**
     * Return the value associated with variable: identifier.
     *
     * @param context an OAuth2 context
     */
    public static @Nullable String getVariableIdentifier(@NotNull OAuth2Context context) {
        return (String) context.get(VARIABLE_IDENTIFIER);
    }

    // ##################################################################################

    @Override
    public boolean isApplicable(
            @NotNull OAuth2PlatformApplication platformApplication,
            boolean isApiRefreshTokenSupported, boolean isApiRevokeAuthSupported) {
        return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Construct a new instance. */
    public ExtraAuthUrlQueryOAuth2ApiPlugin() {
        super(Collections.singletonList(
                new ExtraAuthUrlQueryOAuth2ApiInterceptor()
        ));
    }

}

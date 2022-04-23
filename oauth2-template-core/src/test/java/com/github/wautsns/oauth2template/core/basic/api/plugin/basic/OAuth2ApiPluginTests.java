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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link OAuth2ApiPlugin}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiPluginTests {

    @Test
    void OAuth2ApiPlugin_List_Normal() {
        List<OAuth2ApiInterceptor> interceptors = new ArrayList<>(0);
        NormalOAuth2ApiPlugin instance = new NormalOAuth2ApiPlugin(interceptors);
        assertNotNull(instance);
        assertEquals(interceptors, instance.getInterceptors());
    }

    @Test
    void OAuth2ApiPlugin_List_NullInterceptors() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new NormalOAuth2ApiPlugin(null));
    }

    // ##################################################################################

    static class NormalOAuth2ApiPlugin extends OAuth2ApiPlugin {

        @Override
        public boolean isApplicable(
                @NotNull OAuth2PlatformApplication platformApplication,
                boolean isApiRefreshTokenSupported, boolean isApiRevokeAuthSupported) {
            return true;
        }

        public NormalOAuth2ApiPlugin(@NotNull List<@NotNull OAuth2ApiInterceptor> interceptors) {
            super(interceptors);
        }

    }

}

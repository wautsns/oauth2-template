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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokeneventlistener.interceptor.TokenEventListenerOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link TokenEventListenerOAuth2ApiPlugin}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class TokenEventListenerOAuth2ApiPluginTests {

    @Test
    void isApplicable_OAuth2PlatformApplication$boolean$boolean_Normal() {
        TokenEventListenerOAuth2ApiInterceptor interceptor =
                mock(TokenEventListenerOAuth2ApiInterceptor.class);
        TokenEventListenerOAuth2ApiPlugin instanceA =
                new TokenEventListenerOAuth2ApiPlugin(new ArrayList<>(0));
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        assertFalse(instanceA.isApplicable(platformApplication, true, true));
        TokenEventListenerOAuth2ApiPlugin instanceB =
                new TokenEventListenerOAuth2ApiPlugin(Arrays.asList(interceptor));
        assertTrue(instanceB.isApplicable(platformApplication, true, true));
    }

    @Test
    void isApplicable_OAuth2PlatformApplication$boolean$boolean_NullPlatformApplication() {
        TokenEventListenerOAuth2ApiPlugin instance =
                new TokenEventListenerOAuth2ApiPlugin(new ArrayList<>(0));
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.isApplicable(null, true, true));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void TokenEventListenerOAuth2ApiPlugin_NoArg_Normal() {
        TokenEventListenerOAuth2ApiInterceptor interceptor =
                mock(TokenEventListenerOAuth2ApiInterceptor.class);
        TokenEventListenerOAuth2ApiPlugin instance =
                new TokenEventListenerOAuth2ApiPlugin(Arrays.asList(interceptor));
        List<OAuth2ApiInterceptor> interceptors = instance.getInterceptors();
        assertEquals(1, interceptors.size());
        assertEquals(interceptor, interceptors.get(0));
    }

}

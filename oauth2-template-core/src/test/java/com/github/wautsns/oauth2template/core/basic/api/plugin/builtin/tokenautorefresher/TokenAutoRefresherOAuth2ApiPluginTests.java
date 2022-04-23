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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.tokenautorefresher.interceptor.TokenAutoRefresherOAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for {@link TokenAutoRefresherOAuth2ApiPlugin}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class TokenAutoRefresherOAuth2ApiPluginTests {

    @Test
    void isApplicable_OAuth2PlatformApplication$boolean$boolean_Normal() {
        TokenAutoRefresherOAuth2ApiPlugin instance = new TokenAutoRefresherOAuth2ApiPlugin();
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        assertTrue(instance.isApplicable(platformApplication, true, true));
        assertFalse(instance.isApplicable(platformApplication, false, true));
    }

    @Test
    void isApplicable_OAuth2PlatformApplication$boolean$boolean_NullPlatformApplication() {
        TokenAutoRefresherOAuth2ApiPlugin instance = new TokenAutoRefresherOAuth2ApiPlugin();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.isApplicable(null, true, true));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void TokenAutoRefresherOAuth2ApiPlugin_NoArg_Normal() {
        TokenAutoRefresherOAuth2ApiPlugin instance = new TokenAutoRefresherOAuth2ApiPlugin();
        List<OAuth2ApiInterceptor> interceptors = instance.getInterceptors();
        assertEquals(1, interceptors.size());
        assertInstanceOf(TokenAutoRefresherOAuth2ApiInterceptor.class, interceptors.get(0));
    }

}

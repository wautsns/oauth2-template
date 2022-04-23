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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link OAuth2ApiInterceptor}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiInterceptorTests {

    @Test
    void OAuth2ApiInterceptor_List_Normal() {
        List<OAuth2ApiInterceptPoint> interceptPoints = new ArrayList<>();
        NormalOAuth2ApiInterceptor instance = new NormalOAuth2ApiInterceptor(interceptPoints);
        assertEquals(interceptPoints, instance.getInterceptPoints());
    }

    @Test
    void OAuth2ApiInterceptor_List_NullInterceptorPoints() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new NormalOAuth2ApiInterceptor(null));
    }

    // ##################################################################################

    static class NormalOAuth2ApiInterceptor extends OAuth2ApiInterceptor {

        @Override
        public @UnknownNullability Object invoke(@NotNull OAuth2ApiInvocation invocation)
                throws OAuth2Exception {
            return invocation.proceed();
        }

        public NormalOAuth2ApiInterceptor(
                @NotNull List<@NotNull OAuth2ApiInterceptPoint> interceptPoints) {
            super(interceptPoints);
        }

    }

}

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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2ApiBuildAuthUrl;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2ApiInterceptPoint}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiInterceptPointTest {

    @Test
    void OAuth2ApiInterceptPoint_Class$int_Normal() {
        Class<? extends OAuth2Api<?, ?>> target = OAuth2ApiBuildAuthUrl.class;
        OAuth2ApiInterceptPoint instance = new OAuth2ApiInterceptPoint(target, 123);
        assertSame(target, instance.getTarget());
        assertSame(123, instance.getOrder());
    }

    @Test
    void OAuth2ApiInterceptPoint_Class$int_NullTarget() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new OAuth2ApiInterceptPoint(null, 123));
    }

    @Test
    void OAuth2ApiInterceptPoint_Class$int_IllegalTarget() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OAuth2ApiInterceptPoint(OAuth2Api.class, 123);
        });
    }

}

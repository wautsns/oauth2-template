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
package com.github.wautsns.oauth2template.core.exception.specific;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.api.model.OAuth2DataReceived;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2AccessTokenExpiredException}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2AccessTokenExpiredExceptionTests {

    @Test
    void OAuth2AccessTokenExpiredException_String$OAuth2DataReceived_Normal() {
        OAuth2DataReceived dataReceived = mock(OAuth2DataReceived.class);
        assertDoesNotThrow(() -> new OAuth2AccessTokenExpiredException("message", dataReceived));
    }

    @Test
    void OAuth2AccessTokenExpiredException_String$OAuth2DataReceived_NullDataReceived() {
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions, ThrowableNotThrown
            new OAuth2AccessTokenExpiredException("", null);
        });
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getDataReceived_NoArg_Normal() {
        OAuth2DataReceived dataReceived = mock(OAuth2DataReceived.class);
        OAuth2AccessTokenExpiredException instance =
                new OAuth2AccessTokenExpiredException("message", dataReceived);
        assertEquals("message", instance.getMessage());
        assertSame(dataReceived, instance.getDataReceived());
    }

}

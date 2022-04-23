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
package com.github.wautsns.oauth2template.core.exception;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for {@link OAuth2IOException}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2IOExceptionTests {

    @Test
    void OAuth2IOException_IOException_Normal() {
        IOException cause = new IOException();
        assertDoesNotThrow(() -> new OAuth2IOException(cause));
    }

    @Test
    void OAuth2IOException_IOException_NullCause() {
        // noinspection ConstantConditions, ThrowableNotThrown
        assertThrows(NullPointerException.class, () -> new OAuth2IOException(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getCause_NoArg_Normal() {
        IOException cause = new IOException();
        OAuth2IOException instance = new OAuth2IOException(cause);
        assertSame(cause, instance.getCause());
    }

}

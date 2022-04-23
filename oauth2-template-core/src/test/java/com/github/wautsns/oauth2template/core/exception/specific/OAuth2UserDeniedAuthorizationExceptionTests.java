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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2UserDeniedAuthorizationException}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2UserDeniedAuthorizationExceptionTests {

    @Test
    void OAuth2UserDeniedAuthorizationException_String_Normal() {
        OAuth2UserDeniedAuthorizationException exception =
                new OAuth2UserDeniedAuthorizationException("message");
        assertEquals("message", exception.getMessage());
    }

}

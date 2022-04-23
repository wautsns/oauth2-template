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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2HttpEntity}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpEntityTests {

    @Test
    void toString_NoArg_Normal() {
        assertEquals("[text/plain]:bodyString", new NormalOAuth2HttpEntity().toString());
    }

    // ##################################################################################

    static class NormalOAuth2HttpEntity extends OAuth2HttpEntity<NormalOAuth2HttpEntity> {

        @Override
        public @NotNull String getContentType() {
            return "text/plain";
        }

        @Override
        public @NotNull String getBodyString() {
            return "bodyString";
        }

        @Override
        public @NotNull OAuth2HttpEntityTests.NormalOAuth2HttpEntity copy() {
            return new NormalOAuth2HttpEntity();
        }

    }

}

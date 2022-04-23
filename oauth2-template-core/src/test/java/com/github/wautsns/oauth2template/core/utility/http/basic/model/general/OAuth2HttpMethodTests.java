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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2HttpMethod}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpMethodTests {

    @Test
    void request_String$int_Normal() {
        for (OAuth2HttpMethod instance : OAuth2HttpMethod.values()) {
            OAuth2HttpRequest<?> request = instance.request("https://github.com", 0);
            assertEquals(instance, request.getMethod());
            assertEquals("https://github.com", request.getUrl().getUrlWithoutQuery());
        }
    }

}

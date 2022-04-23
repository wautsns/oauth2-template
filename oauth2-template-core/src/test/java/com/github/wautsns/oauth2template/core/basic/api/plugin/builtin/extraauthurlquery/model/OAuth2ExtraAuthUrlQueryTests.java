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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2UrlQuery;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2ExtraAuthUrlQuery}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ExtraAuthUrlQueryTests {

    @Test
    void OAuth2ExtraAuthUrlQuery_String_Normal() {
        NormalOAuth2ExtraAuthUrlQuery instance = new NormalOAuth2ExtraAuthUrlQuery("id");
        assertEquals("id", instance.getIdentifier());
    }

    @Test
    void OAuth2ExtraAuthUrlQuery_String_NullIdentifier() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new NormalOAuth2ExtraAuthUrlQuery(null));
    }

    // ##################################################################################

    static class NormalOAuth2ExtraAuthUrlQuery extends OAuth2ExtraAuthUrlQuery {

        @Override
        public @NotNull OAuth2Platform getPlatform() {
            return mock(OAuth2Platform.class);
        }

        @Override
        public void encodeAndSetToQuery(@NotNull OAuth2UrlQuery query) {
            query.encodeAndSet("identifier", getIdentifier());
        }

        public NormalOAuth2ExtraAuthUrlQuery(@NotNull String identifier) {
            super(identifier);
        }

    }

}

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
package com.github.wautsns.oauth2template.core.utility.http.basic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.wautsns.oauth2template.core.exception.OAuth2IOException;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link OAuth2HttpClient}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpClientTests {

    @Test
    void execute() throws OAuth2IOException {
        MockOAuth2HttpClient client = new MockOAuth2HttpClient();
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request("http://abc.com", 4);
        assertNotNull(client.execute(request));
    }

    @Test
    void illegalPropertiesImplementation() {
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        properties.setImplementation(MockOAuth2HttpClient.class);
        assertThrows(IllegalArgumentException.class, () -> {
            new OAuth2HttpClient(properties) {
                @Override
                protected @NotNull OAuth2HttpResponse execute(
                        @NotNull OAuth2HttpMethod method, @NotNull String url,
                        @NotNull OAuth2HttpHeaders headers,
                        @Nullable OAuth2HttpEntity<?> entity) throws IOException {
                    return new MockOAuth2HttpResponse();
                }
            };
        });
    }

    @Test
    void throwIOException() {
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request("http://abc.com", 4);
        OAuth2HttpClient client = new OAuth2HttpClient(OAuth2HttpClientProperties.DEFAULTS) {
            @Override
            protected @NotNull OAuth2HttpResponse execute(
                    @NotNull OAuth2HttpMethod method, @NotNull String url,
                    @NotNull OAuth2HttpHeaders headers,
                    @Nullable OAuth2HttpEntity<?> entity) throws IOException {
                throw new IOException();
            }
        };
        assertThrows(OAuth2IOException.class, () -> {
            client.execute(request);
        });
    }

    @Test
    void throwRuntimeException() {
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request("http://abc.com", 4);
        OAuth2HttpClient client = new OAuth2HttpClient(OAuth2HttpClientProperties.DEFAULTS) {
            @Override
            protected @NotNull OAuth2HttpResponse execute(
                    @NotNull OAuth2HttpMethod method, @NotNull String url,
                    @NotNull OAuth2HttpHeaders headers,
                    @Nullable OAuth2HttpEntity<?> entity) throws IOException {
                throw new IllegalStateException();
            }
        };
        assertThrows(IllegalStateException.class, () -> {
            client.execute(request);
        });
    }

    // ##################################################################################

    static class MockOAuth2HttpClient extends OAuth2HttpClient {

        @Override
        protected @NotNull OAuth2HttpResponse execute(
                @NotNull OAuth2HttpMethod method, @NotNull String url,
                @NotNull OAuth2HttpHeaders headers, @Nullable OAuth2HttpEntity<?> entity)
                throws IOException {
            return new MockOAuth2HttpResponse();
        }

        public MockOAuth2HttpClient() {
            super(OAuth2HttpClientProperties.DEFAULTS);
        }

    }

    static class MockOAuth2HttpResponse extends OAuth2HttpResponse {

        @Override
        public int getStatusCode() {
            return 200;
        }

        @Override
        public @Nullable String getHeader(@NotNull String name) {
            return null;
        }

        @Override
        public @NotNull List<@NotNull String> getHeaders(@NotNull String name) {
            return new ArrayList<>(0);
        }

        @Override
        protected @Nullable InputStream getBodyInputStream() throws IOException {
            return null;
        }

    }

}

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.exception.OAuth2IOException;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.builtin.OAuth2UrlEncodedFormHttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for {@link OAuth2HttpClient}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpClientTests {

    @Test
    void execute_OAuth2HttpRequest_Normal() throws IOException, OAuth2IOException {
        OAuth2HttpClient instance = new NormalOAuth2HttpClient();
        OAuth2HttpRequest<?> requestA = OAuth2HttpMethod.GET.request("url", 1);
        requestA.getUrl().getQuery().add("name", "value");
        assertNotNull(instance.execute(requestA));
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> requestB =
                OAuth2HttpMethod.POST.request("url", 1);
        requestB.getUrl().getQuery().add("name", "value");
        requestB.setHeaders(new OAuth2HttpHeaders(1).setAcceptJson());
        requestB.setEntity(new OAuth2UrlEncodedFormHttpEntity(1).add("name", "value"));
        assertNotNull(instance.execute(requestB));
    }

    @Test
    void execute_OAuth2HttpRequest_ThrowIOException() throws Exception {
        OAuth2HttpClient instance = spy(NormalOAuth2HttpClient.class);
        doThrow(IOException.class).when(instance).execute(any(), any(), any(), any());
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request("url", 0);
        assertThrows(OAuth2IOException.class, () -> instance.execute(request));
    }

    @Test
    void execute_OAuth2HttpRequest_ThrowRuntimeException() throws Exception {
        OAuth2HttpClient instance = spy(NormalOAuth2HttpClient.class);
        doThrow(RuntimeException.class).when(instance).execute(any(), any(), any(), any());
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request("url", 0);
        assertThrows(RuntimeException.class, () -> instance.execute(request));
    }

    @Test
    void OAuth2HttpClient_OAuth2HttpClientProperties_Normal() {
        assertDoesNotThrow(() -> new NormalOAuth2HttpClient());
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        properties.setImplementation(NormalOAuth2HttpClient.class);
        assertDoesNotThrow(() -> new NormalOAuth2HttpClient(properties));
    }

    @Test
    void OAuth2HttpClient_OAuth2HttpClientProperties_IllegalImplementation() {
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        properties.setImplementation(mock(OAuth2HttpClient.class).getClass());
        assertThrows(IllegalArgumentException.class, () -> new NormalOAuth2HttpClient(properties));
    }

    // ##################################################################################

    static class NormalOAuth2HttpClient extends OAuth2HttpClient {

        @Override
        protected @NotNull OAuth2HttpResponse execute(
                @NotNull OAuth2HttpMethod method, @NotNull String url,
                @NotNull OAuth2HttpHeaders headers, @Nullable OAuth2HttpEntity<?> entity)
                throws IOException {
            return mock(OAuth2HttpResponse.class);
        }

        public NormalOAuth2HttpClient() {
            super(OAuth2HttpClientProperties.DEFAULTS);
        }

        public NormalOAuth2HttpClient(@NotNull OAuth2HttpClientProperties properties) {
            super(properties);
        }

    }

}

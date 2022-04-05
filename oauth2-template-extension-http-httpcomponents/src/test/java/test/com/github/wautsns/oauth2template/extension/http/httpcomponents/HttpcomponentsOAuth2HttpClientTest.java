/*
 * Copyright 2021 the original author or authors.
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
package test.com.github.wautsns.oauth2template.extension.http.httpcomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.builtin.OAuth2UrlEncodedFormHttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.request.OAuth2HttpRequest;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub;
import com.github.wautsns.oauth2template.extension.http.httpcomponents.HttpcomponentsOAuth2HttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test {@link HttpcomponentsOAuth2HttpClient}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public class HttpcomponentsOAuth2HttpClientTest {

    /** Http client. */
    private static OAuth2HttpClient client;

    // ##################################################################################

    @BeforeClass
    public static void setup() {
        client = OAuth2HttpClientFactoryHub.create();
        assertTrue(client instanceof HttpcomponentsOAuth2HttpClient);
    }

    // ##################################################################################

    @Test
    public void testRequestHeaderRelatedMethods() throws Exception {
        // Initialize request.
        String url = "http://www.httpbin.org/anything";
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request(url, 0);
        request.setHeaders(
                new OAuth2HttpHeaders(2)
                        .encodeAndSet("Unique-Key", "value")
                        .encodeAndAdd("Repeatable-Key", "value1")
                        .encodeAndAdd("Repeatable-Key", "value2")
        );
        // Execute request and assert.
        OAuth2HttpResponse response = client.execute(request);
        assertEquals(200, response.getStatusCode());
        assertFalse(response.getHeaders("Content-Type").isEmpty());
        assertEquals("application/json", response.getHeader("Content-Type"));
        JsonNode body = response.parseBodyAsJson();
        assertNotNull(body);
        JsonNode headers = body.required("headers");
        assertEquals("value", headers.required("Unique-Key").asText());
        assertEquals("value1,value2", headers.required("Repeatable-Key").asText());
    }

    @Test
    public void testRequestGetRelatedMethods() throws Exception {
        // Initialize request.
        String url = "http://www.httpbin.org/anything";
        OAuth2HttpRequest<?> request = OAuth2HttpMethod.GET.request(url, 3);
        request.getUrl().getQuery()
                .encodeAndSet("uniqueKey", "value")
                .encodeAndAdd("repeatableKey", "value1")
                .encodeAndAdd("repeatableKey", "value2");
        // Execute request and assert.
        OAuth2HttpResponse response = client.execute(request);
        assertEquals(200, response.getStatusCode());
        JsonNode body = response.parseBodyAsJson();
        assertNotNull(body);
        JsonNode args = body.required("args");
        assertEquals("value", args.required("uniqueKey").asText());
        assertEquals(2, args.required("repeatableKey").size());
        assertNotNull(body.path("headers").required("User-Agent").asText());
    }

    @Test
    public void testRequestPostRelatedMethods() throws Exception {
        // Initialize request.
        String url = "http://www.httpbin.org/anything";
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> request =
                OAuth2HttpMethod.POST.request(url, 0);
        request.setEntity(
                new OAuth2UrlEncodedFormHttpEntity(4)
                        .set("uniqueKey", "value")
                        .add("repeatableKey", "value1")
                        .add("repeatableKey", "value2")
        );
        // Execute request and assert.
        OAuth2HttpResponse response = client.execute(request);
        assertEquals(200, response.getStatusCode());
        JsonNode body = response.parseBodyAsJson();
        assertNotNull(body);
        assertNotNull(body.path("headers").required("User-Agent").asText());
        JsonNode form = body.required("form");
        assertEquals("value", form.required("uniqueKey").asText());
        assertEquals(2, form.required("repeatableKey").size());
    }

}

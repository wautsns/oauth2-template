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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.builtin.OAuth2UrlEncodedFormHttpEntity;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2HttpRequest}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpRequestTests {

    @Test
    void copy_boolean$boolean$boolean_Normal() {
        OAuth2Url url = new OAuth2Url("https://github.com", 4);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instance =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, url);
        instance.setHeaders(new OAuth2HttpHeaders(4).add("name", "value"));
        instance.setEntity(new OAuth2UrlEncodedFormHttpEntity(4).add("name", "value"));
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> copyA = instance.copy(true, true, true);
        assertSame(instance.getMethod(), copyA.getMethod());
        assertSame(instance.getUrl(), copyA.getUrl());
        assertSame(instance.getHeaders(), copyA.getHeaders());
        assertSame(instance.getEntity(), copyA.getEntity());
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> copyB =
                instance.copy(false, false, false);
        assertSame(instance.getMethod(), copyB.getMethod());
        assertNotSame(instance.getUrl(), copyB.getUrl());
        assertEquals(instance.getUrl(), copyB.getUrl());
        assertNotSame(instance.getHeaders(), copyB.getHeaders());
        assertEquals(instance.getHeaders(), copyB.getHeaders());
        assertNotSame(instance.getEntity(), copyB.getEntity());
        assertEquals(instance.getEntity(), copyB.getEntity());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2HttpRequest_OAuth2HttpMethod$OAuth2Url_Normal() {
        OAuth2Url url = new OAuth2Url("https://github.com", 4);
        OAuth2HttpRequest<?> instance = new OAuth2HttpRequest<>(OAuth2HttpMethod.GET, url);
        assertSame(OAuth2HttpMethod.GET, instance.getMethod());
        assertSame(url, instance.getUrl());
    }

    @Test
    void OAuth2HttpRequest_OAuth2HttpMethod$OAuth2Url_NullMethod() {
        OAuth2Url url = new OAuth2Url("https://github.com", 4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new OAuth2HttpRequest<>(null, url));
    }

    @Test
    void OAuth2HttpRequest_OAuth2HttpMethod$OAuth2Url_NullUrl() {
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2HttpRequest<>(OAuth2HttpMethod.GET, null);
        });
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        OAuth2Url url = new OAuth2Url("https://github.com", 4);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instance =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, url);
        assertEquals("POST https://github.com", instance.toString());
        instance.setHeaders(new OAuth2HttpHeaders(4).add("name", "value"));
        assertEquals("POST https://github.com Headers:{name=value}", instance.toString());
        instance.setEntity(new OAuth2UrlEncodedFormHttpEntity(4).add("name", "value"));
        assertEquals(
                "POST https://github.com Headers:{name=value}" +
                        " Entity:[application/x-www-form-urlencoded]:name=value",
                instance.toString()
        );
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2Url urlA = new OAuth2Url("https://github.com", 3);
        OAuth2Url urlB = new OAuth2Url("https://github.com", 4);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instanceA =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, urlA);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instanceB =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, urlB);
        assertEquals(instanceA, instanceB);
        instanceA.getUrl().getQuery().add("name", "value");
        assertNotEquals(instanceA, instanceB);
        instanceB.getUrl().getQuery().add("name", "value");
        assertEquals(instanceA, instanceB);
        instanceA.setHeaders(new OAuth2HttpHeaders(3).add("name", "value"));
        assertNotEquals(instanceA, instanceB);
        instanceB.setHeaders(new OAuth2HttpHeaders(4).add("name", "value"));
        assertEquals(instanceA, instanceB);
        instanceA.setEntity(new OAuth2UrlEncodedFormHttpEntity(3).add("name", "value"));
        assertNotEquals(instanceA, instanceB);
        instanceB.setEntity(new OAuth2UrlEncodedFormHttpEntity(4).add("name", "value"));
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void hashCode_NoArg_Normal() {
        OAuth2Url urlA = new OAuth2Url("https://github.com", 3);
        OAuth2Url urlB = new OAuth2Url("https://github.com", 4);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instanceA =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, urlA);
        OAuth2HttpRequest<OAuth2UrlEncodedFormHttpEntity> instanceB =
                new OAuth2HttpRequest<>(OAuth2HttpMethod.POST, urlB);
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.getUrl().getQuery().add("name", "value");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.getUrl().getQuery().add("name", "value");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.setHeaders(new OAuth2HttpHeaders(3).add("name", "value"));
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.setHeaders(new OAuth2HttpHeaders(4).add("name", "value"));
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.setEntity(new OAuth2UrlEncodedFormHttpEntity(3).add("name", "value"));
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.setEntity(new OAuth2UrlEncodedFormHttpEntity(4).add("name", "value"));
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
    }

}

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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2HttpHeaders}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpHeadersTests {

    @Test
    void setAccept_String_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        instance.setAccept("application/json");
        assertEquals("application/json", instance.get("Accept"));
        instance.setAccept(null);
        assertNull(instance.get("Accept"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void setAcceptJson_NoArg_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        instance.setAcceptJson();
        assertEquals("application/json", instance.get("Accept"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void setAuthorization_String_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        instance.setAuthorization("Bearer token");
        assertEquals("Bearer token", instance.get("Authorization"));
        instance.setAuthorization(null);
        assertNull(instance.get("Authorization"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void setUserAgentBuiltin_NoArg_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        instance.setUserAgentBuiltin();
        assertEquals(
                "Mozilla/5.0 (X11; Linux x86_64) MyWebKit/98.6.27 OAuth2Template/98.2.5",
                instance.get("User-Agent")
        );
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void copy_NoArg_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        OAuth2HttpHeaders copy = instance.setAcceptJson().copy();
        assertEquals("application/json", copy.get("Accept"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encode_String_Normal() {
        OAuth2HttpHeaders instance = new OAuth2HttpHeaders(4);
        assertEquals("abc", instance.encode("abc"));
        assertEquals("+ /?%#&=", instance.encode("+ /?%#&="));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2HttpHeaders instanceA = new OAuth2HttpHeaders(3);
        OAuth2HttpHeaders instanceB = new OAuth2HttpHeaders(4);
        assertEquals(instanceA, instanceB);
        instanceA.set("nameA", "valueA1");
        assertNotEquals(instanceA, instanceB);
        instanceB.set("nameA", "valueA1");
        assertEquals(instanceA, instanceB);
        instanceA.set("nameA", "valueA2");
        assertNotEquals(instanceA, instanceB);
        instanceB.set("nameA", "valueA2");
        assertEquals(instanceA, instanceB);
        instanceA.add("nameB", "valueB1");
        assertNotEquals(instanceA, instanceB);
        instanceB.add("nameB", "valueB1");
        assertEquals(instanceA, instanceB);
        instanceA.add("nameC", "valueC3");
        instanceA.add("nameC", "valueC4");
        instanceB.add("nameC", "valueC4");
        instanceB.add("nameC", "valueC3");
        assertNotEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void hashCode_NoArg_Normal() {
        OAuth2HttpHeaders instanceA = new OAuth2HttpHeaders(3);
        OAuth2HttpHeaders instanceB = new OAuth2HttpHeaders(4);
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.set("nameA", "valueA1");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.set("nameA", "valueA1");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.set("nameA", "valueA2");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.set("nameA", "valueA2");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.add("nameB", "valueB1");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.add("nameB", "valueB1");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.add("nameC", "valueC3");
        instanceA.add("nameC", "valueC4");
        instanceB.add("nameC", "valueC4");
        instanceB.add("nameC", "valueC3");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
    }

}

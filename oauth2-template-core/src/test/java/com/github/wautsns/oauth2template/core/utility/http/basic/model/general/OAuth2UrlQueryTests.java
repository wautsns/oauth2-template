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

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2UrlQuery}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2UrlQueryTests {

    @Test
    void copy_NoArg_Normal() {
        OAuth2UrlQuery instance = new OAuth2UrlQuery(4);
        instance.add("name", "value");
        OAuth2UrlQuery copy = instance.copy();
        assertEquals("value", copy.get("name"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        OAuth2UrlQuery instance = new OAuth2UrlQuery(4);
        assertEquals("", instance.toString());
        instance.add("nameA", "valueA1");
        assertEquals("?nameA=valueA1", instance.toString());
        instance.add("nameA", "valueA2");
        assertEquals("?nameA=valueA1&nameA=valueA2", instance.toString());
        instance.add("nameB", "valueB1");
        String actual = instance.toString();
        if (actual.indexOf("nameA") < actual.indexOf("nameB")) {
            assertEquals("?nameA=valueA1&nameA=valueA2&nameB=valueB1", actual);
        } else {
            assertEquals("?nameB=valueB1&nameA=valueA1&nameA=valueA2", actual);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encode_String_Normal() {
        OAuth2UrlQuery instance = new OAuth2UrlQuery(4);
        assertEquals("abc", instance.encode("abc"));
        assertEquals("%2B+%2F%3F%25%23%26%3D", instance.encode("+ /?%#&="));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2UrlQuery instanceA = new OAuth2UrlQuery(3);
        OAuth2UrlQuery instanceB = new OAuth2UrlQuery(4);
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
        OAuth2UrlQuery instanceA = new OAuth2UrlQuery(3);
        OAuth2UrlQuery instanceB = new OAuth2UrlQuery(4);
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

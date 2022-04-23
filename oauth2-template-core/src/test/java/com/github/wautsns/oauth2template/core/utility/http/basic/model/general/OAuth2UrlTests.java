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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2Url}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2UrlTests {

    @Test
    void copy_NoArg_Normal() {
        OAuth2Url instance = new OAuth2Url("https://github.com", 4);
        instance.getQuery().add("name", "value");
        instance.setAnchor("anchor");
        OAuth2Url copy = instance.copy();
        assertEquals(instance.getUrlWithoutQuery(), copy.getUrlWithoutQuery());
        assertNotSame(instance.getQuery(), copy.getQuery());
        assertEquals(instance.getQuery(), copy.getQuery());
        assertEquals(instance.getAnchor(), copy.getAnchor());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2Url_String$int_Normal() {
        assertDoesNotThrow(() -> new OAuth2Url("https://github.com", 4));
    }

    @Test
    void OAuth2Url_String$int_NullUrlWithoutQuery() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new OAuth2Url(null, 4));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        OAuth2Url instance = new OAuth2Url("https://github.com", 4);
        assertEquals("https://github.com", instance.toString());
        instance.getQuery().add("name", "value");
        assertEquals("https://github.com?name=value", instance.toString());
        instance.setAnchor("anchor");
        assertEquals("https://github.com?name=value#anchor", instance.toString());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2Url instanceA = new OAuth2Url("https://github.com", 3);
        OAuth2Url instanceB = new OAuth2Url("https://github.com", 4);
        assertEquals(instanceA, instanceB);
        instanceA.getQuery().add("name", "value");
        assertNotEquals(instanceA, instanceB);
        instanceB.getQuery().add("name", "value");
        assertEquals(instanceA, instanceB);
        instanceA.setAnchor("anchor");
        assertNotEquals(instanceA, instanceB);
        instanceB.setAnchor("anchor");
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void hashCode_NoArg_Normal() {
        OAuth2Url instanceA = new OAuth2Url("https://github.com", 3);
        OAuth2Url instanceB = new OAuth2Url("https://github.com", 4);
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.getQuery().add("name", "value");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.getQuery().add("name", "value");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.setAnchor("anchor");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.setAnchor("anchor");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
    }

}

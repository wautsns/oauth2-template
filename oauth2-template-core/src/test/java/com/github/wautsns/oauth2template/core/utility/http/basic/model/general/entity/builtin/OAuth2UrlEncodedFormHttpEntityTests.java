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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2UrlEncodedFormHttpEntity}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2UrlEncodedFormHttpEntityTests {

    @Test
    void getContentType_NoArg_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        assertEquals("application/x-www-form-urlencoded", instance.getContentType());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void getBodyString_NoArg_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        assertEquals("", instance.getBodyString());
        instance.add("nameA", "valueA1");
        assertEquals("nameA=valueA1", instance.getBodyString());
        instance.add("nameA", "valueA2");
        assertEquals("nameA=valueA1&nameA=valueA2", instance.getBodyString());
        instance.add("nameB", "valueB1");
        String actual = instance.getBodyString();
        if (actual.indexOf("nameA") < actual.indexOf("nameB")) {
            assertEquals("nameA=valueA1&nameA=valueA2&nameB=valueB1", actual);
        } else {
            assertEquals("nameB=valueB1&nameA=valueA1&nameA=valueA2", actual);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void isEmpty_NoArg_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        assertTrue(instance.isEmpty());
        instance.add("name", "value");
        assertFalse(instance.isEmpty());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void get_String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        assertNull(instance.get("name"));
        instance.add("name", "value");
        assertEquals("value", instance.get("name"));
    }

    @Test
    void get_String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.get(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void forEach_BiConsumer_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.set("nameA", "valueA1");
        instance.set("nameA", "valueA2");
        instance.add("nameB", "valueB1");
        instance.add("nameB", "valueB2");
        OAuth2UrlEncodedFormHttpEntity reference = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.forEach(reference::add);
        assertEquals(instance, reference);
    }

    @Test
    void forEach_BiConsumer_NullAction() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.forEach(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void set_String$String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.set("name", "value1");
        assertEquals("value1", instance.get("name"));
        instance.set("name", "value2");
        assertEquals("value2", instance.get("name"));
        instance.set("name", null);
        assertNull(instance.get("name"));
    }

    @Test
    void set_String$String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.set(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encodeAndSet_String$String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.encodeAndSet("name", "value1%");
        assertEquals("value1%25", instance.get("name"));
        instance.encodeAndSet("name", "value2%");
        assertEquals("value2%25", instance.get("name"));
        instance.encodeAndSet("name", null);
        assertNull(instance.get("name"));
    }

    @Test
    void encodeAndSet_String$String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.encodeAndSet(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void add_String$String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.add("name", "value1");
        assertEquals("value1", instance.get("name"));
        instance.add("name", "value2");
        assertEquals("value1", instance.get("name"));
        instance.add("name", null);
        assertEquals("value1", instance.get("name"));
    }

    @Test
    void add_String$String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.add(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encodeAndAdd_String$String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.encodeAndAdd("name", "value1%");
        assertEquals("value1%25", instance.get("name"));
        instance.encodeAndAdd("name", "value2%");
        assertEquals("value1%25", instance.get("name"));
        instance.encodeAndAdd("name", null);
        assertEquals("value1%25", instance.get("name"));
    }

    @Test
    void encodeAndAdd_String$String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.encodeAndAdd(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void remove_String_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.remove("name");
        instance.add("name", "value");
        instance.remove("name");
        assertNull(instance.get("name"));
    }

    @Test
    void remove_String_NullName() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.remove(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2HttpMultiValueMap_OAuth2HttpMultiValueMap_Copy() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.set("nameA", "valueA1");
        instance.set("nameA", "valueA2");
        instance.add("nameB", "valueB1");
        OAuth2UrlEncodedFormHttpEntity copy = instance.copy();
        assertNotSame(instance, copy);
        assertEquals(instance, copy);
        instance.add("nameB", "valueB2");
        assertNotEquals(instance, copy);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void copy_NoArg_Normal() {
        OAuth2UrlEncodedFormHttpEntity instance = new OAuth2UrlEncodedFormHttpEntity(4);
        instance.add("nameA", "valueA1");
        instance.add("nameA", "valueA2");
        instance.set("nameB", "valueB1");
        OAuth2UrlEncodedFormHttpEntity copy = instance.copy();
        assertNotSame(instance, copy);
        assertEquals(instance, copy);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2UrlEncodedFormHttpEntity instanceA = new OAuth2UrlEncodedFormHttpEntity(3);
        OAuth2UrlEncodedFormHttpEntity instanceB = new OAuth2UrlEncodedFormHttpEntity(4);
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
        OAuth2UrlEncodedFormHttpEntity instanceA = new OAuth2UrlEncodedFormHttpEntity(3);
        OAuth2UrlEncodedFormHttpEntity instanceB = new OAuth2UrlEncodedFormHttpEntity(4);
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

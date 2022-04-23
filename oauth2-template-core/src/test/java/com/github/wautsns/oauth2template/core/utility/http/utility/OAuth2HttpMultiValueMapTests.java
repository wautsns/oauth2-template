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
package com.github.wautsns.oauth2template.core.utility.http.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OAuth2HttpMultiValueMap}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpMultiValueMapTests {

    @Test
    void isEmpty_NoArg_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        assertTrue(instance.isEmpty());
        instance.add("name", "value");
        assertFalse(instance.isEmpty());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void get_String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        assertNull(instance.get("name"));
        instance.add("name", "value");
        assertEquals("value", instance.get("name"));
    }

    @Test
    void get_String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.get(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void forEach_BiConsumer_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.set("nameA", "valueA1");
        instance.set("nameA", "valueA2");
        instance.add("nameB", "valueB1");
        instance.add("nameB", "valueB2");
        NormalOAuth2HttpMultiValueMap reference = new NormalOAuth2HttpMultiValueMap();
        instance.forEach(reference::add);
        assertEquals(instance, reference);
    }

    @Test
    void forEach_BiConsumer_NullAction() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.forEach(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void set_String$String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.set("name", "value1");
        assertEquals("value1", instance.get("name"));
        instance.set("name", "value2");
        assertEquals("value2", instance.get("name"));
        instance.set("name", null);
        assertNull(instance.get("name"));
    }

    @Test
    void set_String$String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.set(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encodeAndSet_String$String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.encodeAndSet("name", "value1");
        assertEquals("value1___", instance.get("name"));
        instance.encodeAndSet("name", "value2");
        assertEquals("value2___", instance.get("name"));
        instance.encodeAndSet("name", null);
        assertNull(instance.get("name"));
    }

    @Test
    void encodeAndSet_String$String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.encodeAndSet(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void add_String$String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.add("name", "value1");
        assertEquals("value1", instance.get("name"));
        instance.add("name", "value2");
        assertEquals("value1", instance.get("name"));
        instance.add("name", null);
        assertEquals("value1", instance.get("name"));
    }

    @Test
    void add_String$String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.add(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void encodeAndAdd_String$String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.encodeAndAdd("name", "value1");
        assertEquals("value1___", instance.get("name"));
        instance.encodeAndAdd("name", "value2");
        assertEquals("value1___", instance.get("name"));
        instance.encodeAndAdd("name", null);
        assertEquals("value1___", instance.get("name"));
    }

    @Test
    void encodeAndAdd_String$String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.encodeAndAdd(null, ""));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void remove_String_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.remove("name");
        instance.add("name", "value");
        instance.remove("name");
        assertNull(instance.get("name"));
    }

    @Test
    void remove_String_NullName() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> instance.remove(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2HttpMultiValueMap_OAuth2HttpMultiValueMap_Copy() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.set("nameA", "valueA1");
        instance.set("nameA", "valueA2");
        instance.add("nameB", "valueB1");
        NormalOAuth2HttpMultiValueMap copy = instance.copy();
        assertNotSame(instance, copy);
        assertEquals(instance, copy);
        instance.add("nameB", "valueB2");
        assertNotEquals(instance, copy);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        NormalOAuth2HttpMultiValueMap instance = new NormalOAuth2HttpMultiValueMap();
        instance.add("nameA", "valueA1");
        assertEquals("{nameA=valueA1}", instance.toString());
        instance.add("nameA", "valueA2");
        assertEquals("{nameA=[valueA1, valueA2]}", instance.toString());
        instance.add("nameB", "valueB1");
        String actual = instance.toString();
        if (actual.indexOf("nameA") < actual.indexOf("nameB")) {
            assertEquals("{nameA=[valueA1, valueA2], nameB=valueB1}", actual);
        } else {
            assertEquals("{nameB=valueB1, nameA=[valueA1, valueA2]}", actual);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        NormalOAuth2HttpMultiValueMap instanceA = new NormalOAuth2HttpMultiValueMap(3);
        NormalOAuth2HttpMultiValueMap instanceB = new NormalOAuth2HttpMultiValueMap(4);
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
        NormalOAuth2HttpMultiValueMap instanceA = new NormalOAuth2HttpMultiValueMap(3);
        NormalOAuth2HttpMultiValueMap instanceB = new NormalOAuth2HttpMultiValueMap(4);
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

    // ##################################################################################

    static class NormalOAuth2HttpMultiValueMap
            extends OAuth2HttpMultiValueMap<NormalOAuth2HttpMultiValueMap> {

        @Override
        public @NotNull String encode(@NotNull String text) {
            return text + "___";
        }

        @Override
        public @NotNull NormalOAuth2HttpMultiValueMap copy() {
            return new NormalOAuth2HttpMultiValueMap(this);
        }

        public NormalOAuth2HttpMultiValueMap() {
            super(4);
        }

        public NormalOAuth2HttpMultiValueMap(int initialCapacity) {
            super(initialCapacity);
        }

        protected NormalOAuth2HttpMultiValueMap(NormalOAuth2HttpMultiValueMap template) {
            super(template);
        }

    }

}

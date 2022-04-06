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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests for {@link OAuth2HttpMultiValueMap}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpMultiValueMapTests {

    @Test
    void isEmpty() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        assertTrue(map.isEmpty());
        map.set("name", "value");
        assertFalse(map.isEmpty());
    }

    @Test
    void get() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        assertNull(map.get("name"));
        map.add("name", "value1");
        assertEquals("value1", map.get("name"));
        map.add("name", "value2");
        assertEquals("value1", map.get("name"));
    }

    @Test
    void forEach() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.add("nameA", "valueA");
        map.add("nameB", "valueB1");
        map.add("nameB", "valueB2");
        List<String> cache = new ArrayList<>(3);
        map.forEach((name, value) -> cache.add(name + ":" + value));
        cache.sort(Comparator.naturalOrder());
        Assertions.assertIterableEquals(
                Arrays.asList("nameA:valueA", "nameB:valueB1", "nameB:valueB2"),
                cache
        );
    }

    @Test
    void set() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.set("name", "value1");
        assertEquals("value1", map.get("name"));
        assertEquals(1, map.size());
        map.set("name", "value2");
        assertEquals(1, map.size());
        assertEquals("value2", map.get("name"));
        map.set("name", null);
        assertEquals(0, map.size());
        assertNull(map.get("name"));
    }

    @Test
    void encodeAndSet() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.encodeAndSet("name", "value1");
        assertEquals(1, map.size());
        assertEquals("value1___", map.get("name"));
        map.encodeAndSet("name", "value2");
        assertEquals(1, map.size());
        assertEquals("value2___", map.get("name"));
        map.encodeAndSet("name", null);
        assertEquals(0, map.size());
        assertNull(map.get("name"));
    }

    @Test
    void add() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.add("name", "value1");
        assertEquals(1, map.size());
        assertEquals("value1", map.get("name"));
        map.add("name", "value2");
        assertEquals(2, map.size());
        assertEquals("value1", map.get("name"));
        map.add("name", "value3");
        assertEquals(3, map.size());
        assertEquals("value1", map.get("name"));
        map.add("name", null);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("name"));
        List<String> cache = new ArrayList<>(3);
        map.forEach((name, value) -> cache.add(name + ":" + value));
        cache.sort(Comparator.naturalOrder());
        Assertions.assertIterableEquals(
                Arrays.asList("name:value1", "name:value2", "name:value3"),
                cache
        );
    }

    @Test
    void encodeAndAdd() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.encodeAndAdd("name", "value1");
        assertEquals(1, map.size());
        assertEquals("value1___", map.get("name"));
        map.encodeAndAdd("name", "value2");
        assertEquals(2, map.size());
        assertEquals("value1___", map.get("name"));
        map.encodeAndAdd("name", "value3");
        assertEquals(3, map.size());
        assertEquals("value1___", map.get("name"));
        map.encodeAndAdd("name", null);
        assertEquals(3, map.size());
        assertEquals("value1___", map.get("name"));
        List<String> cache = new ArrayList<>(3);
        map.forEach((name, value) -> cache.add(name + ":" + value));
        cache.sort(Comparator.naturalOrder());
        Assertions.assertIterableEquals(
                Arrays.asList("name:value1___", "name:value2___", "name:value3___"),
                cache
        );
    }

    @Test
    void remove() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.add("name", "value");
        map.remove("name");
        assertNull(map.get("name"));
        assertTrue(map.isEmpty());
    }

    @Test
    void copy() {
        MockOAuth2HttpMultiValueMap mapA = new MockOAuth2HttpMultiValueMap(4);
        mapA.add("nameA", "valueA1");
        mapA.add("nameA", "valueA2");
        mapA.add("nameB", "valueB");
        MockOAuth2HttpMultiValueMap mapB = mapA.copy();
        assertEquals("valueA1", mapB.get("nameA"));
        assertEquals("valueB", mapB.get("nameB"));
        List<String> cache = new ArrayList<>(3);
        mapB.forEach((name, value) -> cache.add(name + ":" + value));
        cache.sort(Comparator.naturalOrder());
        Assertions.assertIterableEquals(
                Arrays.asList("nameA:valueA1", "nameA:valueA2", "nameB:valueB"),
                cache
        );
    }

    @Test
    void testToString() {
        MockOAuth2HttpMultiValueMap map = new MockOAuth2HttpMultiValueMap(4);
        map.add("name", "valueA");
        assertEquals("{name=valueA}", map.toString());
        map.add("name", "valueB");
        assertEquals("{name=[valueA, valueB]}", map.toString());
    }

    // ##################################################################################

    static class MockOAuth2HttpMultiValueMap
            extends OAuth2HttpMultiValueMap<MockOAuth2HttpMultiValueMap> {

        @Override
        public @NotNull String encode(@NotNull String text) {
            return text + "___";
        }

        @Override
        public @NotNull MockOAuth2HttpMultiValueMap copy() {
            return new MockOAuth2HttpMultiValueMap(this);
        }

        public MockOAuth2HttpMultiValueMap(int initialCapacity) {
            super(initialCapacity);
        }

        protected MockOAuth2HttpMultiValueMap(@NotNull MockOAuth2HttpMultiValueMap template) {
            super(template);
        }

        // ##################################################################################

        @TestOnly
        public int size() {
            AtomicInteger size = new AtomicInteger(0);
            forEach((name, value) -> {
                size.incrementAndGet();
            });
            return size.get();
        }

    }

}

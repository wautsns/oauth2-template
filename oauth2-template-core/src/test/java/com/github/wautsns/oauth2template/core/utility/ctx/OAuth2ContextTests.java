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
package com.github.wautsns.oauth2template.core.utility.ctx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tests for {@link OAuth2Context}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ContextTests {

    @Test
    void hashMap_NoArg_Normal() {
        OAuth2Context instanceA = OAuth2Context.hashMap();
        OAuth2Context instanceB = OAuth2Context.delegate(new HashMap<>());
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void concurrentHashMap_NoArg_Normal() {
        OAuth2Context instanceA = OAuth2Context.concurrentHashMap();
        OAuth2Context instanceB = OAuth2Context.delegate(new ConcurrentHashMap<>());
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void delegate_Map_Normal() {
        OAuth2Context instanceA = OAuth2Context.delegate(new HashMap<>(3));
        OAuth2Context instanceB = OAuth2Context.delegate(new HashMap<>(4));
        assertEquals(instanceA, instanceB);
    }

    @Test
    void delegate_Map_NullDelegate() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> OAuth2Context.delegate(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void entrySet_NoArg_Normal() {
        OAuth2Context instance = OAuth2Context.hashMap();
        instance.put("name", "value");
        assertEquals(1, instance.entrySet().size());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void put_String$String_Normal() {
        OAuth2Context instance = OAuth2Context.hashMap();
        instance.put("name", "value1");
        assertEquals("value1", instance.get("name"));
        instance.put("name", "value2");
        assertEquals("value2", instance.get("name"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2Context instanceA = OAuth2Context.hashMap();
        OAuth2Context instanceB = OAuth2Context.hashMap();
        assertEquals(instanceA, instanceB);
        instanceA.put("name", "value");
        assertNotEquals(instanceA, instanceB);
        instanceB.put("name", "value");
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void hashCode_NoArg_Normal() {
        OAuth2Context instanceA = OAuth2Context.hashMap();
        OAuth2Context instanceB = OAuth2Context.hashMap();
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.put("name", "value");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.put("name", "value");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
    }

}

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
package com.github.wautsns.oauth2template.core.basic.model.application;

import static com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub.Manipulation.registerIfAbsent;
import static com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub.acquire;
import static com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub.find;
import static com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub.optional;
import static com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests for {@link OAuth2ApplicationHub}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApplicationHubTests {

    private static final String MOCK_NAME_PREFIX = OAuth2ApplicationHubTests.class.getName() + "#";

    private static String mockName(String name) {
        return MOCK_NAME_PREFIX + "#" + name;
    }

    // ##################################################################################

    @Test
    void find_String_Normal() {
        String name = mockName("find_String_Normal");
        OAuth2Application instanceA = find(name);
        assertNull(instanceA);
        registerIfAbsent(name);
        OAuth2Application instanceB = find(name);
        assertNotNull(instanceB);
        assertEquals(name, instanceB.getName());
    }

    @Test
    void find_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> find(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void optional_String_Normal() {
        String name = mockName("optional_String_Normal");
        Optional<OAuth2Application> optionalA = optional(name);
        assertFalse(optionalA.isPresent());
        registerIfAbsent(name);
        Optional<OAuth2Application> optionalB = optional(name);
        assertTrue(optionalB.isPresent());
        assertEquals(name, optionalB.get().getName());
    }

    @Test
    void optional_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> optional(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void acquire_String_Normal() {
        String name = mockName("acquire_String_Normal");
        registerIfAbsent(name);
        OAuth2Application instance = acquire(name);
        assertNotNull(instance);
        assertEquals(name, instance.getName());
    }

    @Test
    void acquire_String_NullName() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> acquire(null));
    }

    @Test
    void acquire_String_NotExists() {
        String name = mockName("acquire_String_NotExists");
        assertThrows(IllegalArgumentException.class, () -> acquire(name));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void stream_NoArg_Normal() {
        String name = mockName("stream_NoArg_Normal");
        registerIfAbsent(name);
        assertTrue(stream().anyMatch(item -> name.equals(item.getName())));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nested
    class ManipulationTests {

        @Test
        void registerIfAbsent_String_Normal() {
            String name = mockName("registerIfAbsent_String_Normal");
            OAuth2Application instanceA = registerIfAbsent(name);
            OAuth2Application instanceB = registerIfAbsent(name);
            assertSame(instanceA, instanceB);
            assertEquals(name, instanceA.getName());
        }

        @Test
        void registerIfAbsent_String_NullName() {
            // noinspection ConstantConditions
            assertThrows(NullPointerException.class, () -> registerIfAbsent(null));
        }

    }

}

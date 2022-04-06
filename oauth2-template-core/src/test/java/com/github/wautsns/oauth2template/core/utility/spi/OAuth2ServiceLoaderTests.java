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
package com.github.wautsns.oauth2template.core.utility.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

/**
 * Tests for {@link OAuth2ServiceLoader}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ServiceLoaderTests {

    @Test
    void load() {
        OAuth2ServiceLoader<OAuth2Service> serviceLoader =
                OAuth2ServiceLoader.load(OAuth2Service.class);
        Iterator<OAuth2Service> iterator;
        for (int i = 0; i < 2; i++) {
            iterator = serviceLoader.iterator();
            assertTrue(iterator.hasNext());
            assertInstanceOf(OAuth2ServiceProviderA.class, iterator.next());
            assertTrue(iterator.hasNext());
            assertInstanceOf(OAuth2ServiceProviderB.class, iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void loadInstalled() {
        OAuth2ServiceLoader<OAuth2Service> serviceLoader =
                OAuth2ServiceLoader.loadInstalled(OAuth2Service.class);
        assertFalse(serviceLoader.iterator().hasNext());
    }

    @Test
    void reload() {
        OAuth2ServiceLoader<OAuth2Service> serviceLoader =
                OAuth2ServiceLoader.load(OAuth2Service.class);
        OAuth2Service providerA1 = serviceLoader.iterator().next();
        assertInstanceOf(OAuth2ServiceProviderA.class, providerA1);
        serviceLoader.reload();
        OAuth2Service providerA2 = serviceLoader.iterator().next();
        assertInstanceOf(OAuth2ServiceProviderA.class, providerA1);
        assertNotSame(providerA1, providerA2);
    }

    @Test
    void toStringA() {
        OAuth2ServiceLoader<OAuth2Service> serviceLoader =
                OAuth2ServiceLoader.load(OAuth2Service.class);
        assertEquals(
                "OAuth2ServiceLoader[com.github.wautsns.oauth2template.core.utility.spi" +
                        ".OAuth2ServiceLoaderTests$OAuth2Service]",
                serviceLoader.toString()
        );
    }

    // ##################################################################################

    static class OAuth2Service {}

    static class OAuth2ServiceProviderA extends OAuth2Service {}

    static class OAuth2ServiceProviderB extends OAuth2Service {}

}

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

import static com.github.wautsns.oauth2template.core.utility.spi.OAuth2ServiceLoader.load;
import static com.github.wautsns.oauth2template.core.utility.spi.OAuth2ServiceLoader.loadInstalled;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * Tests for {@link OAuth2ServiceLoader}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ServiceLoaderTests {

    @Test
    void load_Class_Normal() {
        Iterator<OAuth2Service> iterator = load(OAuth2Service.class).iterator();
        assertTrue(iterator.hasNext());
        assertInstanceOf(OAuth2ServiceProviderA.class, iterator.next());
        assertTrue(iterator.hasNext());
        assertInstanceOf(OAuth2ServiceProviderB.class, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void load_Class_NoConfigurationFile() {
        assertFalse(load(String.class).iterator().hasNext());
    }

    @Test
    void load_Class_NullService() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> load(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void load_Class$ClassLoader_Normal() {
        Iterator<OAuth2Service> iterator = load(OAuth2Service.class).iterator();
        assertTrue(iterator.hasNext());
        assertInstanceOf(OAuth2ServiceProviderA.class, iterator.next());
        assertTrue(iterator.hasNext());
        assertInstanceOf(OAuth2ServiceProviderB.class, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void load_Class$ClassLoader_NullService() {
        ClassLoader loader = getClass().getClassLoader();
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> load(null, loader));
    }

    @Test
    void load_Class$ClassLoader_NullClassLoader() {
        Iterator<Date> iterator = load(Date.class, null).iterator();
        assertTrue(iterator.hasNext());
        assertInstanceOf(Date.class, iterator.next());
        assertFalse(iterator.hasNext());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void loadInstalled_Class_Normal() {
        assertFalse(loadInstalled(Date.class).iterator().hasNext());
    }

    @Test
    void loadInstalled_Class_NullService() {
        // noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> loadInstalled(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void reload_NoArg_Normal() {
        OAuth2ServiceLoader<OAuth2Service> instance = load(OAuth2Service.class);
        OAuth2Service providerA1 = instance.iterator().next();
        assertInstanceOf(OAuth2ServiceProviderA.class, providerA1);
        instance.reload();
        Iterator<OAuth2Service> iteratorB = instance.iterator();
        OAuth2Service providerB1 = iteratorB.next();
        assertInstanceOf(OAuth2ServiceProviderA.class, providerB1);
        assertNotSame(providerA1, providerB1);
        OAuth2Service providerB2 = iteratorB.next();
        assertInstanceOf(OAuth2ServiceProviderB.class, providerB2);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void iterator_NoArg_Normal() {
        OAuth2ServiceLoader<OAuth2Service> instance = load(OAuth2Service.class);
        Iterator<OAuth2Service> iterator;
        for (int i = 0; i < 2; i++) {
            iterator = instance.iterator();
            assertTrue(iterator.hasNext());
            assertNotNull(iterator.next());
            assertTrue(iterator.hasNext());
            assertNotNull(iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void iterator_NoArg_ConfigurationFormatError() {
        List<Class<?>> serviceList = Arrays.asList(
                OAuth2ServiceIllegalConfigurationSyntax.class,
                OAuth2ServiceIllegalProviderClassNameStart.class,
                OAuth2ServiceIllegalProviderClassNamePart.class,
                OAuth2ServiceProviderCouldNotBeInstantiated.class,
                OAuth2ServiceProviderNotASubtype.class,
                OAuth2ServiceProviderNotFound.class
        );
        for (Class<?> service : serviceList) {
            assertThrows(ServiceConfigurationError.class, load(service).iterator()::next);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void toString_NoArg_Normal() {
        assertEquals(
                "OAuth2ServiceLoader[com.github.wautsns.oauth2template.core.utility.spi" +
                        ".OAuth2ServiceLoaderTests$OAuth2Service]",
                load(OAuth2Service.class).toString()
        );
    }

    // ##################################################################################

    static class OAuth2Service {}

    static class OAuth2ServiceProviderA extends OAuth2Service {}

    static class OAuth2ServiceProviderB extends OAuth2Service {}

    static class OAuth2ServiceIllegalConfigurationSyntax {}

    static class OAuth2ServiceIllegalProviderClassNameStart {}

    static class OAuth2ServiceIllegalProviderClassNamePart {}

    static class OAuth2ServiceProviderNotFound {}

    static class OAuth2ServiceProviderNotASubtype {}

    static class OAuth2ServiceProviderCouldNotBeInstantiated {

        public OAuth2ServiceProviderCouldNotBeInstantiated(String ignored) {}

    }

}

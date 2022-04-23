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
package com.github.wautsns.oauth2template.core.utility.http.basic.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties.ProxyProperties;

import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Tests for {@link OAuth2HttpClientProperties}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpClientPropertiesTests {

    @Test
    void STATIC_FIELD__DEFAULTS_NORMAL() {
        OAuth2HttpClientProperties instance = OAuth2HttpClientProperties.DEFAULTS;
        assertEquals(OAuth2HttpClient.class, instance.getImplementation());
        assertEquals(Duration.parse("PT5S"), instance.getConnectionTimeout());
        assertEquals(Duration.parse("PT3S"), instance.getSocketTimeout());
        assertEquals(64, instance.getMaxConcurrentRequests());
        assertEquals(Duration.parse("PT5M"), instance.getMaxIdleTime());
        assertEquals(Duration.parse("PT3M"), instance.getDefaultKeepAliveTimeout());
        assertEquals(1, instance.getRetryTimes());
        assertNull(instance.getProxy());
        assertNotNull(instance.getCustom());
        assertTrue(instance.getCustom().isEmpty());
    }

    @Test
    void STATIC_FIELD__DEFAULTS__CUSTOM() {
        OAuth2HttpClientProperties instance = OAuth2HttpClientProperties.DEFAULTS;
        Object previous = instance.getCustom().setProperty("name", "value1");
        OAuth2HttpClientProperties copy = instance.copy();
        assertEquals("value1", copy.getCustom().getProperty("name"));
        instance.getCustom().setProperty("name", "value2");
        assertEquals("value2", copy.getCustom().getProperty("name"));
        copy.getCustom().setProperty("name", "value2");
        instance.getCustom().setProperty("name", "value3");
        assertEquals("value2", copy.getCustom().getProperty("name"));
        if (previous == null) {
            instance.getCustom().remove("name");
        } else {
            instance.getCustom().put("name", previous);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void copy_NoArg_Normal() {
        OAuth2HttpClientProperties instance = new OAuth2HttpClientProperties();
        ProxyProperties proxy = new ProxyProperties();
        proxy.setHost("host");
        proxy.setPort(12345);
        proxy.setUsername("username");
        proxy.setPassword("password");
        instance.setProxy(proxy);
        instance.getCustom().setProperty("name", "value");
        OAuth2HttpClientProperties copy = instance.copy();
        assertEquals(instance.getImplementation(), copy.getImplementation());
        assertEquals(instance.getConnectionTimeout(), copy.getConnectionTimeout());
        assertEquals(instance.getSocketTimeout(), copy.getSocketTimeout());
        assertEquals(instance.getMaxConcurrentRequests(), copy.getMaxConcurrentRequests());
        assertEquals(instance.getMaxIdleTime(), copy.getMaxIdleTime());
        assertEquals(instance.getDefaultKeepAliveTimeout(), copy.getDefaultKeepAliveTimeout());
        assertEquals(instance.getRetryTimes(), copy.getRetryTimes());
        assertNotNull(copy.getProxy());
        assertNotSame(instance.getProxy(), copy.getProxy());
        ProxyProperties proxyCopy = copy.getProxy();
        assertEquals(proxy.getHost(), proxyCopy.getHost());
        assertEquals(proxy.getPort(), proxyCopy.getPort());
        assertEquals(proxy.getUsername(), proxyCopy.getUsername());
        assertEquals(proxy.getPassword(), proxyCopy.getPassword());
        assertNotNull(copy.getCustom());
        assertNotSame(instance.getCustom(), copy.getCustom());
        assertEquals(instance.getCustom(), copy.getCustom());
        assertEquals("value", copy.getCustom().getProperty("name"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void equals_Object_Normal() {
        OAuth2HttpClientProperties instanceA = new OAuth2HttpClientProperties();
        OAuth2HttpClientProperties instanceB = new OAuth2HttpClientProperties();
        assertEquals(instanceA, instanceB);
        instanceA.setProxy(new ProxyProperties());
        instanceA.getProxy().setUsername("username");
        assertNotEquals(instanceA, instanceB);
        instanceB.setProxy(new ProxyProperties());
        instanceB.getProxy().setUsername("username");
        assertEquals(instanceA, instanceB);
        instanceA.getCustom().put("name", "value");
        assertNotEquals(instanceA, instanceB);
        instanceB.getCustom().put("name", "value");
        assertEquals(instanceA, instanceB);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void hashCode_NoArg_Normal() {
        OAuth2HttpClientProperties instanceA = new OAuth2HttpClientProperties();
        OAuth2HttpClientProperties instanceB = new OAuth2HttpClientProperties();
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.setProxy(new ProxyProperties());
        instanceA.getProxy().setUsername("username");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.setProxy(new ProxyProperties());
        instanceB.getProxy().setUsername("username");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceA.getCustom().put("name", "value");
        assertNotEquals(instanceA.hashCode(), instanceB.hashCode());
        instanceB.getCustom().put("name", "value");
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
    }

}

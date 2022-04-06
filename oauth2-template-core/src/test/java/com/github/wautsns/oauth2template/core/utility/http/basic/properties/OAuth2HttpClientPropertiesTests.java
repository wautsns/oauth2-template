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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;

import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Tests for {@link OAuth2HttpClientProperties}
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpClientPropertiesTests {

    @Test
    void defaultValues() {
        OAuth2HttpClientProperties properties = OAuth2HttpClientProperties.DEFAULTS;
        assertEquals(OAuth2HttpClient.class, properties.getImplementation());
        assertEquals(Duration.parse("PT5S"), properties.getConnectionTimeout());
        assertEquals(Duration.parse("PT3S"), properties.getSocketTimeout());
        assertEquals(64, properties.getMaxConcurrentRequests());
        assertEquals(Duration.parse("PT5M"), properties.getMaxIdleTime());
        assertEquals(Duration.parse("PT3M"), properties.getDefaultKeepAliveTimeout());
        assertEquals(1, properties.getRetryTimes());
        assertNull(properties.getProxy());
        assertNotNull(properties.getCustom());
        assertTrue(properties.getCustom().isEmpty());
    }

    @Test
    void copy() {
        OAuth2HttpClientProperties properties = new OAuth2HttpClientProperties();
        OAuth2HttpClientProperties.ProxyProperties proxy =
                new OAuth2HttpClientProperties.ProxyProperties();
        proxy.setHost("host");
        proxy.setPort(80);
        proxy.setUsername("username");
        proxy.setPassword("password");
        properties.setProxy(proxy);
        OAuth2HttpClientProperties copy = properties.copy();
        assertEquals(properties.getImplementation(), copy.getImplementation());
        assertEquals(properties.getConnectionTimeout(), copy.getConnectionTimeout());
        assertEquals(properties.getSocketTimeout(), copy.getSocketTimeout());
        assertEquals(properties.getMaxConcurrentRequests(), copy.getMaxConcurrentRequests());
        assertEquals(properties.getMaxIdleTime(), copy.getMaxIdleTime());
        assertEquals(properties.getDefaultKeepAliveTimeout(), copy.getDefaultKeepAliveTimeout());
        assertEquals(properties.getRetryTimes(), copy.getRetryTimes());
        assertNotNull(copy.getProxy());
        assertNotSame(properties.getProxy(), copy.getProxy());
        OAuth2HttpClientProperties.ProxyProperties proxyCopy = copy.getProxy();
        assertEquals(proxy.getHost(), proxyCopy.getHost());
        assertEquals(proxy.getPort(), proxyCopy.getPort());
        assertEquals(proxy.getUsername(), proxyCopy.getUsername());
        assertEquals(proxy.getPassword(), proxyCopy.getPassword());
        assertNotNull(copy.getCustom());
        assertTrue(copy.getCustom().isEmpty());
        assertNotSame(properties.getCustom(), copy.getCustom());
    }

}

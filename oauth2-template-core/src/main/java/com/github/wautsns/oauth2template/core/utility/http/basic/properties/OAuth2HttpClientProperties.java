/*
 * Copyright 2021 the original author or authors.
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

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * OAuth2 http client properties.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2HttpClientProperties {

    /**
     * Default properties.
     *
     * <ul>
     * <li style="list-style-type:none">########## Default Properties ###############</li>
     * <li>implementation: {@link OAuth2HttpClient OAuth2HttpClient.class}</li>
     * <li>connectionTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT5S")}</li>
     * <li>socketTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT3S")}</li>
     * <li>maxConcurrentRequests: {@code 64}</li>
     * <li>maxIdleTime: {@link Duration#parse(CharSequence) Duration.parse("PT5M")}</li>
     * <li>defaultKeepAliveTimeout: {@link Duration#parse(CharSequence)
     * Duration.parse("PT3M")}</li>
     * <li>retryTimes: {@code 1}</li>
     * <li>proxy: {@code null} (type:{@link ProxyProperties})</li>
     * <li>custom: {@link Properties#Properties(Properties) new Properties()}</li>
     * </ul>
     */
    public static final @NotNull OAuth2HttpClientProperties DEFAULTS =
            new OAuth2HttpClientProperties(null)
                    .setImplementation(OAuth2HttpClient.class)
                    .setConnectionTimeout(Duration.parse("PT5S"))
                    .setSocketTimeout(Duration.parse("PT3S"))
                    .setMaxConcurrentRequests(64)
                    .setMaxIdleTime(Duration.parse("PT5M"))
                    .setDefaultKeepAliveTimeout(Duration.parse("PT3M"))
                    .setRetryTimes(1)
                    .setProxy(null);

    // ##################################################################################

    /**
     * Implementation class.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the value is {@link OAuth2HttpClient}, it means that these properties applies
     * to all classes which are assignable to {@link OAuth2HttpClient}.</li>
     * </ul>
     */
    private @NotNull Class<? extends OAuth2HttpClient> implementation;

    /** Timeout of connection. */
    private Duration connectionTimeout;
    /** Timeout of socket. */
    private Duration socketTimeout;
    /** Max number of concurrent request. */
    private Integer maxConcurrentRequests;
    /** Max idle time of connection. */
    private Duration maxIdleTime;
    /** Default keep alive timeout of connection. */
    private Duration defaultKeepAliveTimeout;
    /** Retry times. */
    private Integer retryTimes;
    /** Proxy properties. */
    private ProxyProperties proxy;

    /** Custom properties. */
    private final @NotNull Properties custom;

    // ##################################################################################

    /**
     * Create a new instance by deep copying this instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>For property <em>custom</em>, copy it through <code>{@link
     * Properties#Properties(Properties) new Properties}(<i>this</i>.custom)</code>.</li>
     * </ul>
     *
     * @return a copy of this instance
     */
    public @NotNull OAuth2HttpClientProperties copy() {
        return new OAuth2HttpClientProperties(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance by deep copying {@link #DEFAULTS}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>For property <em>custom</em>, copy it through <code>{@link
     * Properties#Properties(Properties) new Properties}(<i>this</i>.custom)</code>.</li>
     * </ul>
     */
    public OAuth2HttpClientProperties() {
        this(DEFAULTS);
    }

    /**
     * Construct a new instance by deep copying the given template if it is not {@code null},
     * otherwise it is simply initialize.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>For property <em>custom</em>, copy it through <code>{@link
     * Properties#Properties(Properties) new Properties}(<i>this</i>.custom)</code>.</li>
     * </ul>
     *
     * @param template a template for deep copy, or {@code null} just for simple
     *         initialization
     * @see #copy()
     */
    private OAuth2HttpClientProperties(@Nullable OAuth2HttpClientProperties template) {
        if (template == null) {
            this.implementation = OAuth2HttpClient.class;
            this.custom = new Properties();
        } else {
            this.implementation = template.implementation;
            this.connectionTimeout = template.connectionTimeout;
            this.socketTimeout = template.socketTimeout;
            this.maxConcurrentRequests = template.maxConcurrentRequests;
            this.maxIdleTime = template.maxIdleTime;
            this.defaultKeepAliveTimeout = template.defaultKeepAliveTimeout;
            this.retryTimes = template.retryTimes;
            if (template.proxy != null) {
                this.proxy = new ProxyProperties();
                this.proxy.setHost(template.proxy.getHost());
                this.proxy.setPort(template.proxy.getPort());
                this.proxy.setUsername(template.proxy.getUsername());
                this.proxy.setPassword(template.proxy.getPassword());
            }
            this.custom = new Properties(template.custom);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull Class<? extends OAuth2HttpClient> getImplementation() {
        return implementation;
    }

    public @NotNull OAuth2HttpClientProperties setImplementation(
            @NotNull Class<? extends OAuth2HttpClient> implementation) {
        this.implementation = implementation;
        return this;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public @NotNull OAuth2HttpClientProperties setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public Duration getSocketTimeout() {
        return socketTimeout;
    }

    public @NotNull OAuth2HttpClientProperties setSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public Integer getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public @NotNull OAuth2HttpClientProperties setMaxConcurrentRequests(
            Integer maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        return this;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public @NotNull OAuth2HttpClientProperties setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public Duration getDefaultKeepAliveTimeout() {
        return defaultKeepAliveTimeout;
    }

    public @NotNull OAuth2HttpClientProperties setDefaultKeepAliveTimeout(
            Duration defaultKeepAliveTimeout) {
        this.defaultKeepAliveTimeout = defaultKeepAliveTimeout;
        return this;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public @NotNull OAuth2HttpClientProperties setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public ProxyProperties getProxy() {
        return proxy;
    }

    public @NotNull OAuth2HttpClientProperties setProxy(ProxyProperties proxy) {
        this.proxy = proxy;
        return this;
    }

    public @NotNull Properties getCustom() {
        return custom;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        OAuth2HttpClientProperties that = (OAuth2HttpClientProperties) obj;
        if (!implementation.equals(that.implementation)) {return false;}
        if (!Objects.equals(connectionTimeout, that.connectionTimeout)) {return false;}
        if (!Objects.equals(socketTimeout, that.socketTimeout)) {return false;}
        if (!Objects.equals(maxConcurrentRequests, that.maxConcurrentRequests)) {return false;}
        if (!Objects.equals(maxIdleTime, that.maxIdleTime)) {return false;}
        if (!Objects.equals(defaultKeepAliveTimeout, that.defaultKeepAliveTimeout)) {return false;}
        if (!Objects.equals(retryTimes, that.retryTimes)) {return false;}
        if (!Objects.equals(proxy, that.proxy)) {return false;}
        return custom.equals(that.custom);
    }

    @Override
    public int hashCode() {
        int result = implementation.hashCode();
        result = 31 * result + Objects.hashCode(connectionTimeout);
        result = 31 * result + Objects.hashCode(socketTimeout);
        result = 31 * result + Objects.hashCode(maxConcurrentRequests);
        result = 31 * result + Objects.hashCode(maxIdleTime);
        result = 31 * result + Objects.hashCode(defaultKeepAliveTimeout);
        result = 31 * result + Objects.hashCode(retryTimes);
        result = 31 * result + Objects.hashCode(proxy);
        result = 31 * result + custom.hashCode();
        return result;
    }

    // ##################################################################################

    /** Proxy properties based on username and password. */
    public static final class ProxyProperties {

        /** Proxy host. */
        private String host;
        /** Proxy port. */
        private int port;
        /** Proxy credential: username. */
        private String username;
        /** Proxy credential: password. */
        private String password;

        // ##################################################################################

        public String getHost() {
            return host;
        }

        public @NotNull ProxyProperties setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPort() {
            return port;
        }

        public @NotNull ProxyProperties setPort(int port) {
            this.port = port;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public @NotNull ProxyProperties setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public @NotNull ProxyProperties setPassword(String password) {
            this.password = password;
            return this;
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {return true;}
            if (obj == null || getClass() != obj.getClass()) {return false;}
            ProxyProperties that = (ProxyProperties) obj;
            if (port != that.port) {return false;}
            if (!Objects.equals(host, that.host)) {return false;}
            if (!Objects.equals(username, that.username)) {return false;}
            return Objects.equals(password, that.password);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(host);
            result = 31 * result + port;
            result = 31 * result + Objects.hashCode(username);
            result = 31 * result + Objects.hashCode(password);
            return result;
        }

    }

}

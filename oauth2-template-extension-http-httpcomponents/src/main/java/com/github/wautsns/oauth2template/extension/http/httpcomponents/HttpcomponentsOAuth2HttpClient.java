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
package com.github.wautsns.oauth2template.extension.http.httpcomponents;

import com.github.wautsns.oauth2template.core.utility.http.basic.OAuth2HttpClient;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpHeaders;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2HttpMethod;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.entity.OAuth2HttpEntity;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.response.OAuth2HttpResponse;
import com.github.wautsns.oauth2template.core.utility.http.basic.properties.OAuth2HttpClientProperties;
import com.github.wautsns.oauth2template.extension.http.httpcomponents.model.HttpcomponentsOAuth2HttpResponse;
import com.github.wautsns.oauth2template.extension.http.httpcomponents.plugin.HttpcomponentsConnectionKeepAliveStrategy;
import com.github.wautsns.oauth2template.extension.http.httpcomponents.plugin.HttpcomponentsHttpRequestRetryHandler;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * OAuth2 http client based on apache httpcomponents.
 *
 * @author wautsns
 * @see <a href="https://hc.apache.org/httpcomponents-client-4.5.x/index.html">apache
 *         httpcomponents-client-4.5.x</a>
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public class HttpcomponentsOAuth2HttpClient extends OAuth2HttpClient {

    /** A {@link HttpClient} instance used as delegate. */
    private final @NotNull HttpClient delegate;

    // ##################################################################################

    @Override
    protected final @NotNull OAuth2HttpResponse execute(
            @NotNull OAuth2HttpMethod method, @NotNull String url,
            @NotNull OAuth2HttpHeaders headers, @Nullable OAuth2HttpEntity<?> entity)
            throws IOException {
        HttpRequestBase request = initializeRequest(method, url);
        headers.forEach(request::addHeader);
        if ((entity != null) && (request instanceof HttpEntityEnclosingRequestBase)) {
            StringEntity entityDelegate = new StringEntity(entity.getBodyString());
            entityDelegate.setContentType(entity.getContentType());
            ((HttpEntityEnclosingRequestBase) request).setEntity(entityDelegate);
        }
        return new HttpcomponentsOAuth2HttpResponse(delegate.execute(request));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * @param properties http client properties
     */
    public HttpcomponentsOAuth2HttpClient(@NotNull OAuth2HttpClientProperties properties) {
        super(properties);
        HttpClientBuilder builder = HttpClientBuilder.create();
        // Set request config.
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        Duration connectTimeout = properties.getConnectionTimeout();
        if (connectTimeout != null) {
            requestConfigBuilder.setConnectTimeout((int) connectTimeout.toMillis());
        }
        Duration socketTimeout = properties.getSocketTimeout();
        if (socketTimeout != null) {
            requestConfigBuilder.setSocketTimeout((int) socketTimeout.toMillis());
        }
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        // Set connection manager.
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        Integer maxConcurrentRequests = properties.getMaxConcurrentRequests();
        if (maxConcurrentRequests != null) {
            connectionManager.setMaxTotal(maxConcurrentRequests);
            connectionManager.setDefaultMaxPerRoute(maxConcurrentRequests);
        }
        builder.setConnectionManager(connectionManager);
        // Set max idle time.
        Duration maxIdleTime = properties.getMaxIdleTime();
        if (maxIdleTime != null) {
            builder.evictIdleConnections(maxIdleTime.toMillis(), TimeUnit.MILLISECONDS);
        }
        // Set keep alive strategy.
        Duration keepAliveTimeout = properties.getDefaultKeepAliveTimeout();
        if (keepAliveTimeout != null) {
            builder.setKeepAliveStrategy(
                    new HttpcomponentsConnectionKeepAliveStrategy(keepAliveTimeout));
        }
        // Set retry handler.
        Integer retryTimes = properties.getRetryTimes();
        if ((retryTimes != null) && (retryTimes >= 1)) {
            builder.setRetryHandler(new HttpcomponentsHttpRequestRetryHandler(retryTimes));
        }
        // Set proxy.
        OAuth2HttpClientProperties.ProxyProperties proxy = properties.getProxy();
        if ((proxy != null) && (proxy.getHost() != null)) {
            builder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
            if (proxy.getUsername() != null) {
                BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                UsernamePasswordCredentials credentials =
                        new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword());
                credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                builder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }
        // Extend builder through custom properties.
        extendBuilderThroughProperties(builder, properties);
        // Build apache http client.
        this.delegate = builder.build();
    }

    /**
     * Extend the given builder through the given properties.
     *
     * @param builder a http client builder
     * @param props http client properties
     */
    protected void extendBuilderThroughProperties(
            @NotNull HttpClientBuilder builder, @NotNull OAuth2HttpClientProperties props) {
        // Do nothing by default.
    }

    // ##################################################################################

    /**
     * Initialize a new http request.
     *
     * @param method http method
     * @param url target URL
     * @return a new http request
     */
    private static @NotNull HttpRequestBase initializeRequest(
            @NotNull OAuth2HttpMethod method, @NotNull String url) {
        switch (method) {
            case GET:
                return new HttpGet(url);
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case PATCH:
                return new HttpPatch(url);
            case DELETE:
                return new HttpDelete(url);
            case OPTIONS:
                return new HttpOptions(url);
            case HEAD:
                return new HttpHead(url);
            default:
                throw new IllegalStateException();
        }
    }

}

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

import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactory;
import com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactoryHub;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Simple OAuth2 service-provider loading facility.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>All methods are basically equivalent to {@link ServiceLoader}, except for the location and
 * naming of configuration file.</li>
 * <li>Service-providers are identified by placing configuration files in the resource directory
 * <em>META-INF/wautsns/oauth2template/spi</em>.</li>
 * <li>The name of configuration file is a simple class name and the file type is
 * <em>providers</em>. For example, <em>OAuth2HttpClientFactory.providers</em>.</li>
 * <li>The content of configuration file can refer to {@link ServiceLoader}.</li>
 * </ul>
 *
 * <ul>
 * <li style="list-style-type:none">########## Builtin Supported Services ###############</li>
 * <li>{@link OAuth2HttpClientFactory}({@link OAuth2HttpClientFactoryHub})</li>
 * FIXME [Builtin Supported Services] to be added
 * </ul>
 *
 * @param <S> the type of the service to be loaded
 * @author wautsns
 * @see ServiceLoader
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2ServiceLoader<S> implements Iterable<S> {

    /** The configuration file location format. */
    public static final @NotNull String CONFIGURATION_FILE_LOCATION_FORMAT =
            "META-INF/wautsns/oauth2template/spi/%s.providers";

    // ##################################################################################

    /** The class representing the service. */
    private final @NotNull Class<S> service;
    /** The class loader for locating, loading, and instantiating providers. */
    private final @Nullable ClassLoader loader;
    /** The access control context in which this instance was constructed. */
    private final @Nullable AccessControlContext accessControlContext;
    /** Cached providers, in instantiation order. */
    private final @NotNull LinkedHashMap<@NotNull String, @NotNull S> providers;
    /** An iterator implementing fully-lazy provider lookup. */
    private @NotNull LazyIterator lookupIterator;

    // ##################################################################################

    /**
     * Create a new service loader for the given service, using the current thread's {@linkplain
     * Thread#getContextClassLoader context class loader} as loader.
     *
     * @param <S> the type of the service to be loaded
     * @param service a class representing the service to be loaded
     * @return a new service loader
     * @see ServiceLoader#load(Class)
     * @see #load(Class, ClassLoader)
     */
    public static <S> @NotNull OAuth2ServiceLoader<@NotNull S> load(@NotNull Class<S> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return load(service, loader);
    }

    /**
     * Create a new service loader for the given service class and class loader.
     *
     * @param <S> the type of the service to be loaded
     * @param service a class representing the service to be loaded
     * @param loader a class loader to be used to load configuration files and provider
     *         classes, or {@code null} if the system class loader (or, failing that, the bootstrap
     *         class loader) is to be used
     * @return a new service loader
     * @see ServiceLoader#load(Class, ClassLoader)
     */
    public static <S> @NotNull OAuth2ServiceLoader<@NotNull S> load(
            @NotNull Class<S> service, @Nullable ClassLoader loader) {
        return new OAuth2ServiceLoader<>(service, loader);
    }

    /**
     * Create a new service loader for the given service class, using the extension class loader as
     * loader.
     *
     * @param <S> the type of the service to be loaded
     * @param service a class representing the service to be loaded
     * @return a new service loader
     * @see ServiceLoader#loadInstalled(Class)
     * @see #load(Class, ClassLoader)
     */
    public static <S> @NotNull OAuth2ServiceLoader<@NotNull S> loadInstalled(
            @NotNull Class<S> service) {
        ClassLoader curr = ClassLoader.getSystemClassLoader();
        ClassLoader prev = null;
        while (curr != null) {
            prev = curr;
            curr = curr.getParent();
        }
        return load(service, prev);
    }

    // ##################################################################################

    /**
     * Clear this loader's provider cache so that all providers will be reloaded.
     *
     * @return self reference
     * @see ServiceLoader#reload()
     */
    public @NotNull OAuth2ServiceLoader<@NotNull S> reload() {
        providers.clear();
        lookupIterator = new LazyIterator();
        return this;
    }

    /**
     * Return an iterator that lazily loads providers for this loader's service.
     *
     * @return an iterator
     * @see ServiceLoader#iterator()
     */
    @Override
    public @NotNull Iterator<@NotNull S> iterator() {
        return new Iterator<S>() {

            final Iterator<Map.Entry<String, S>> knownProviders = providers.entrySet().iterator();

            @Override
            public boolean hasNext() {
                if (knownProviders.hasNext()) {
                    return true;
                } else {
                    return lookupIterator.hasNext();
                }
            }

            @Override
            public S next() {
                if (knownProviders.hasNext()) {
                    return knownProviders.next().getValue();
                } else {
                    return lookupIterator.next();
                }
            }

        };
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance for the given service and class loader.
     *
     * @param service a class representing the service to be loaded
     * @param loader a class loader to be used to load configuration files and provider
     *         classes, or {@code null} if the system class loader (or, failing that, the bootstrap
     *         class loader) is to be used
     */
    private OAuth2ServiceLoader(@NotNull Class<S> service, @Nullable ClassLoader loader) {
        this.service = Objects.requireNonNull(service, "Service interface cannot be null");
        this.loader = loader;
        if (System.getSecurityManager() == null) {
            this.accessControlContext = null;
        } else {
            this.accessControlContext = AccessController.getContext();
        }
        this.providers = new LinkedHashMap<>();
        this.lookupIterator = new LazyIterator();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return a string describing this service.
     *
     * <ul>
     * <li style="list-style-type:none">########## Examples ###############</li>
     * <li>OAuth2ServiceLoader[com.github.wautsns.oauth2template.core.utility.http.factory.OAuth2HttpClientFactory]</li>
     * </ul>
     *
     * @return a descriptive string
     */
    @Override
    public @NotNull String toString() {
        return "OAuth2ServiceLoader[" + service.getName() + "]";
    }

    // ##################################################################################

    /** Private inner class implementing fully-lazy provider lookup. */
    private final class LazyIterator implements Iterator<S> {

        /** Enumeration of configuration file URLs. */
        private @Nullable Enumeration<@NotNull URL> configs = null;
        /** An iterator for traversing pending provider class names. */
        private @Nullable Iterator<@NotNull String> pending = null;
        /** Next provider class name. */
        private @Nullable String nextName = null;

        // ##################################################################################

        @Override
        public boolean hasNext() {
            if (accessControlContext == null) {
                return hasNextProvider();
            } else {
                PrivilegedAction<Boolean> action = this::hasNextProvider;
                return AccessController.doPrivileged(action, accessControlContext);
            }
        }

        @Override
        public @NotNull S next() {
            if (accessControlContext == null) {
                return nextProvider();
            } else {
                PrivilegedAction<S> action = this::nextProvider;
                return AccessController.doPrivileged(action, accessControlContext);
            }
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private boolean hasNextProvider() {
            if (nextName != null) {return true;}
            if (configs == null) {
                try {
                    String fileName = service.getSimpleName();
                    String location = String.format(CONFIGURATION_FILE_LOCATION_FORMAT, fileName);
                    if (loader == null) {
                        configs = ClassLoader.getSystemResources(location);
                    } else {
                        configs = loader.getResources(location);
                    }
                } catch (IOException e) {
                    throw error("Error locating configurations", e);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }

        private @NotNull S nextProvider() {
            if (!hasNextProvider()) {throw new NoSuchElementException();}
            String name = nextName;
            nextName = null;
            Class<?> clazz;
            try {
                clazz = Class.forName(name, false, loader);
            } catch (ClassNotFoundException e) {
                throw error("Provider " + name + " not found", e);
            }
            if (!service.isAssignableFrom(clazz)) {
                throw error("Provider " + name + " not a subtype", null);
            }
            try {
                S provider = service.cast(clazz.newInstance());
                // noinspection ConstantConditions
                providers.put(name, provider);
                return provider;
            } catch (Exception e) {
                throw error("Provider " + name + " could not be instantiated", e);
            }
        }

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Parse the content of the given URL as configuration file.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Duplicate provider class names in configuration files will only be traversed once.</li>
     * </ul>
     *
     * @param url a configuration file URL
     * @return an iterator (possibly empty) of provider class names
     * @throws ServiceConfigurationError if an I/O error occurs while reading from the given
     *         URL, or a configuration format error is detected
     */
    private @NotNull Iterator<@NotNull String> parse(@NotNull URL url) {
        ArrayList<String> providerNames = new ArrayList<>();
        try (InputStream input = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            // noinspection StatementWithEmptyBody
            for (int lineNumber = 1; parseLine(reader, url, lineNumber++, providerNames); ) ;
        } catch (IOException e) {
            throw error("Error reading configuration", e);
        }
        return providerNames.iterator();
    }

    /**
     * Read a line through the given configuration file reader, if there is a valid provider class
     * name on the line, add it to the param <em>providerNames</em>.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Duplicate provider class names in configuration files will only be added once.</li>
     * </ul>
     *
     * @param reader a configuration file reader
     * @param url the URL of the configuration file being read
     * @param lineNumber current line number
     * @param providerNames a provider class name list
     * @return {@code true} if the end of the configuration file is not reached, otherwise {@code
     *         false}
     * @throws IOException if an I/O error occurs on reading
     * @throws ServiceConfigurationError if a configuration format error is detected
     */
    private boolean parseLine(
            @NotNull BufferedReader reader, @NotNull URL url, int lineNumber,
            @NotNull List<@NotNull String> providerNames) throws IOException {
        String line = reader.readLine();
        if (line == null) {return false;}
        int commentIndex = line.indexOf('#');
        String providerName;
        if (commentIndex == -1) {
            providerName = line.trim();
        } else {
            providerName = line.substring(0, commentIndex).trim();
        }
        if (providerName.isEmpty()) {return true;}
        if ((providerName.indexOf(' ') >= 0) || (providerName.indexOf('\t') >= 0)) {
            throw error(url, lineNumber, "Illegal configuration syntax");
        }
        int codePoint = providerName.codePointAt(0);
        if (!Character.isJavaIdentifierStart(codePoint)) {
            throw error(url, lineNumber, "Illegal provider class name: " + line);
        }
        for (
                int i = Character.charCount(codePoint);
                i < providerName.length();
                i += Character.charCount(codePoint)) {
            codePoint = providerName.codePointAt(i);
            if (!Character.isJavaIdentifierPart(codePoint) && (codePoint != '.')) {
                throw error(url, lineNumber, "Illegal provider class name: " + providerName);
            }
        }
        if (!providers.containsKey(providerName) && !providerNames.contains(providerName)) {
            providerNames.add(providerName);
        }
        return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private @NotNull ServiceConfigurationError error(@NotNull String message) {
        return new ServiceConfigurationError(service.getName() + ": " + message);
    }

    private @NotNull ServiceConfigurationError error(
            @NotNull String message, @Nullable Throwable cause) {
        return new ServiceConfigurationError(service.getName() + ": " + message, cause);
    }

    private @NotNull ServiceConfigurationError error(
            @NotNull URL url, int lineNumber, @NotNull String message) {
        return error(url + ":" + lineNumber + ": " + message);
    }

}

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
package com.github.wautsns.oauth2template.core.basic.model;

import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2Application;
import com.github.wautsns.oauth2template.core.basic.model.application.OAuth2ApplicationHub;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2PlatformHub;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract OAuth2 platform application.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public abstract class OAuth2PlatformApplication {

    /** Platform. */
    protected final @NotNull OAuth2Platform platform;
    /** Application. */
    protected final @NotNull OAuth2Application application;
    /** Thread-safe OAuth2 context. */
    protected final @NotNull OAuth2Context context;

    // ##################################################################################

    /**
     * Validate this instance.
     *
     * @throws IllegalArgumentException if a property error is detected
     */
    public abstract void validate();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance for the given application name and platform name.
     *
     * @param platformName a platform name
     * @param applicationName an application name
     * @see OAuth2PlatformHub.Manipulation#registerIfAbsent(String)
     * @see OAuth2ApplicationHub.Manipulation#registerIfAbsent(String)
     */
    protected OAuth2PlatformApplication(
            @NotNull String platformName, @NotNull String applicationName) {
        this.platform = OAuth2PlatformHub.Manipulation.registerIfAbsent(platformName);
        this.application = OAuth2ApplicationHub.Manipulation.registerIfAbsent(applicationName);
        this.context = OAuth2Context.concurrentHashMap();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final @NotNull OAuth2Platform getPlatform() {
        return platform;
    }

    public final @NotNull OAuth2Application getApplication() {
        return application;
    }

    public final @NotNull OAuth2Context getContext() {
        return context;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

}

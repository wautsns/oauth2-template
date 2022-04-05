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
package com.github.wautsns.oauth2template.core.basic.model.platform;

import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 platform.
 *
 * @author wautsns
 * @see OAuth2PlatformHub
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2Platform {

    /** Platform name. */
    private final @NotNull String name;
    /** Thread-safe OAuth2 context. */
    private final @NotNull OAuth2Context context;

    // ##################################################################################

    /**
     * Construct a new instance for the given platform name.
     *
     * @param name a platform name
     * @see OAuth2PlatformHub.Manipulation#registerIfAbsent(String)
     */
    OAuth2Platform(@NotNull String name) {
        this.name = name;
        this.context = OAuth2Context.concurrentHashMap();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull String getName() {
        return name;
    }

    public @NotNull OAuth2Context getContext() {
        return context;
    }

}

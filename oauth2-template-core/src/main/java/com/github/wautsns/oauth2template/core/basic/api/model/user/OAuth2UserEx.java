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
package com.github.wautsns.oauth2template.core.basic.api.model.user;

import com.github.wautsns.oauth2template.core.basic.api.model.user.properties.OAuth2UserOpenIdSupplier;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Complete OAuth2 user that implements all properties.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>In order to facilitate the use in certain situations, this class implements all
 * properties.</li>
 * </ul>
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
public final class OAuth2UserEx extends OAuth2User
        implements OAuth2UserOpenIdSupplier {

    /** The delegate of this instance. */
    private final @NotNull OAuth2User delegate;

    // ##################################################################################

    @Override
    public @NotNull OAuth2Platform getPlatform() {
        return delegate.getPlatform();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public @NotNull String getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public @Nullable String getOpenId() {
        if (delegate instanceof OAuth2UserOpenIdSupplier) {
            return ((OAuth2UserOpenIdSupplier) delegate).getOpenId();
        } else {
            return null;
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Construct a new instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>All modification on this instance will be fed back to the delegate and vice versa.</li>
     * </ul>
     *
     * @param delegate an {@link OAuth2User} instance used as delegate
     */
    public OAuth2UserEx(@NotNull OAuth2User delegate) {
        super(delegate.getRawData(), delegate.getTimestamp(), delegate.getContext());
        this.delegate = delegate;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public @NotNull OAuth2User getDelegate() {
        return delegate;
    }

}

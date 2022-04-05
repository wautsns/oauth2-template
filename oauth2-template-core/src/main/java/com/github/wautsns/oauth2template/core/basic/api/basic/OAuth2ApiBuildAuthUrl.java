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
package com.github.wautsns.oauth2template.core.basic.api.basic;

import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract OAuth2Api: <em>BuildAuthURL</em>.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
@FunctionalInterface
public interface OAuth2ApiBuildAuthUrl extends OAuth2Api<OAuth2Context, OAuth2Url> {

    /**
     * Build a new auth URL.
     *
     * @param context an OAuth2 context
     * @return a new auth URL
     * @throws OAuth2Exception if an OAuth2 related error occurs
     */
    @Override
    @NotNull OAuth2Url execute(@Nullable OAuth2Context context) throws OAuth2Exception;

}

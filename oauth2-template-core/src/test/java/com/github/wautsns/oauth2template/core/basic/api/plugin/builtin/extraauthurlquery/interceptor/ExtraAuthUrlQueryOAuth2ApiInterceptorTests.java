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
package com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptPoint;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation.OAuth2ApiInvocation;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.BuiltinOAuth2ApiInterceptorPoints;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.ExtraAuthUrlQueryOAuth2ApiPlugin;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQuery;
import com.github.wautsns.oauth2template.core.basic.api.plugin.builtin.extraauthurlquery.model.OAuth2ExtraAuthUrlQueryHub;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.basic.model.platform.OAuth2Platform;
import com.github.wautsns.oauth2template.core.utility.ctx.OAuth2Context;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2Url;
import com.github.wautsns.oauth2template.core.utility.http.basic.model.general.OAuth2UrlQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for {@link ExtraAuthUrlQueryOAuth2ApiInterceptor}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class ExtraAuthUrlQueryOAuth2ApiInterceptorTests {

    private static final String MOCK_NAME_PREFIX =
            ExtraAuthUrlQueryOAuth2ApiInterceptorTests.class.getName() + "#";

    private static String mockPlatformName(String name) {
        return MOCK_NAME_PREFIX + "#" + name;
    }

    private static OAuth2ApiInvocation initMockedOAuth2ApiInvocation(
            String platformName, OAuth2Context input) throws Exception {
        OAuth2ApiInvocation invocation = mock(OAuth2ApiInvocation.class);
        doReturn(new OAuth2Url("ttp://abc.com", 4)).when(invocation).proceed();
        doReturn(input).when(invocation).getInput();
        OAuth2Platform platform = new OAuth2Platform(platformName);
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        doReturn(platform).when(platformApplication).getPlatform();
        doReturn(platformApplication).when(invocation).getPlatformApplication();
        return invocation;
    }

    // ##################################################################################

    @Test
    void invoke_OAuth2ApiInvocation_Normal() throws Exception {
        String platformName = mockPlatformName("invoke_OAuth2ApiInvocation_Normal");
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        OAuth2Context input = OAuth2Context.hashMap();
        ExtraAuthUrlQueryOAuth2ApiPlugin.setVariableIdentifier(input, "identifier");
        OAuth2ApiInvocation invocation = initMockedOAuth2ApiInvocation(platformName, input);
        OAuth2ExtraAuthUrlQuery extraQuery = mock(OAuth2ExtraAuthUrlQuery.class);
        doReturn(new OAuth2Platform(platformName)).when(extraQuery).getPlatform();
        doReturn("identifier").when(extraQuery).getIdentifier();
        doAnswer(inv -> {
            OAuth2UrlQuery query = inv.getArgument(0);
            query.set("name", "value");
            return null;
        }).when(extraQuery).encodeAndSetToQuery(any());
        OAuth2ExtraAuthUrlQueryHub.Manipulation.register(extraQuery);
        OAuth2Url authUrl = (OAuth2Url) instance.invoke(invocation);
        assertEquals("value", authUrl.getQuery().get("name"));
    }

    @Test
    void invoke_OAuth2ApiInvocation_NullInput() throws Exception {
        String platformName = mockPlatformName("invoke_OAuth2ApiInvocation_NullInput");
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        OAuth2ApiInvocation invocation = initMockedOAuth2ApiInvocation(platformName, null);
        OAuth2Url authUrl = (OAuth2Url) instance.invoke(invocation);
        assertTrue(authUrl.getQuery().isEmpty());
    }

    @Test
    void invoke_OAuth2ApiInvocation_IdentifierNotExist() throws Exception {
        String platformName = mockPlatformName("invoke_OAuth2ApiInvocation_Normal");
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        OAuth2Context input = OAuth2Context.hashMap();
        OAuth2ApiInvocation invocation = initMockedOAuth2ApiInvocation(platformName, input);
        OAuth2Url authUrl = (OAuth2Url) instance.invoke(invocation);
        assertTrue(authUrl.getQuery().isEmpty());
    }

    @Test
    void invoke_OAuth2ApiInvocation_ExtraQueryNotExist() throws Exception {
        String platformName = mockPlatformName("invoke_OAuth2ApiInvocation_Normal");
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        OAuth2Context input = OAuth2Context.hashMap();
        ExtraAuthUrlQueryOAuth2ApiPlugin.setVariableIdentifier(input, "identifier");
        OAuth2ApiInvocation invocation = initMockedOAuth2ApiInvocation(platformName, input);
        OAuth2Url authUrl = (OAuth2Url) instance.invoke(invocation);
        assertTrue(authUrl.getQuery().isEmpty());
    }

    @Test
    void invoke_OAuth2ApiInvocation_NullInvocation() {
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        // noinspection ConstantConditions
        Assertions.assertThrows(NullPointerException.class, () -> instance.invoke(null));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void ExtraAuthUrlQueryOAuth2ApiInterceptor_NoArg_Normal() {
        ExtraAuthUrlQueryOAuth2ApiInterceptor instance =
                new ExtraAuthUrlQueryOAuth2ApiInterceptor();
        List<OAuth2ApiInterceptPoint> points = instance.getInterceptPoints();
        assertEquals(1, points.size());
        assertEquals(
                BuiltinOAuth2ApiInterceptorPoints.EXTRA_AUTH_URL_QUERY_X_BUILD_AUTH_URL,
                points.get(0)
        );
    }

}

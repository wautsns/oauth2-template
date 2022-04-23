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
package com.github.wautsns.oauth2template.core.basic.api.plugin.basic.invocation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.wautsns.oauth2template.core.basic.api.basic.OAuth2Api;
import com.github.wautsns.oauth2template.core.basic.api.plugin.basic.interceptor.OAuth2ApiInterceptor;
import com.github.wautsns.oauth2template.core.basic.model.OAuth2PlatformApplication;
import com.github.wautsns.oauth2template.core.exception.OAuth2Exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link OAuth2ApiInvocationFactory}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiInvocationFactoryTests {

    @Test
    void create_Object_Normal() throws OAuth2Exception {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        doAnswer(inv -> inv.getArgument(0)).when(api).execute(any());
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        Object inputAfterIntercepted = new Object();
        doAnswer(inv -> {
            OAuth2ApiInvocation invocation = inv.getArgument(0);
            invocation.setInput(inputAfterIntercepted);
            return invocation.proceed();
        }).when(interceptor).invoke(any());
        List<OAuth2ApiInterceptor> interceptors = Arrays.asList(interceptor);
        OAuth2ApiInvocationFactory instance =
                new OAuth2ApiInvocationFactory(platformApplication, api, interceptors);
        OAuth2ApiInvocation invocation = instance.create(null);
        assertNull(invocation.getInput());
        Object result = invocation.proceed();
        assertSame(inputAfterIntercepted, invocation.getInput());
        assertSame(inputAfterIntercepted, result);
        verify(api, times(1)).execute(any());
        verify(interceptor, times(1)).invoke(any());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2ApiInvocationFactory_OAuth2PlatformApplication$OAuth2Api$List_Normal() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        List<OAuth2ApiInterceptor> interceptors = Arrays.asList(interceptor);
        assertDoesNotThrow(() -> {
            new OAuth2ApiInvocationFactory(platformApplication, api, interceptors);
        });
    }

    @Test
    void OAuth2ApiInvocationFactory_OAuth2PlatformApplication$OAuth2Api$List_NullPlatformApplication() {
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        List<OAuth2ApiInterceptor> interceptors = Arrays.asList(interceptor);
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocationFactory(null, api, interceptors);
        });
    }

    @Test
    void OAuth2ApiInvocationFactory_OAuth2PlatformApplication$OAuth2Api$List_NullApi() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        List<OAuth2ApiInterceptor> interceptors = Arrays.asList(interceptor);
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocationFactory(platformApplication, null, interceptors);
        });
    }

    @Test
    void OAuth2ApiInvocationFactory_OAuth2PlatformApplication$OAuth2Api$List_NullInterceptors() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocationFactory(platformApplication, api, null);
        });
    }

}

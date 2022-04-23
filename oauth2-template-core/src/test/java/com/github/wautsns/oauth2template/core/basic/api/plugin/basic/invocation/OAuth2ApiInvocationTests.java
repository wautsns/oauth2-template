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

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.util.Iterator;

/**
 * Tests for {@link OAuth2ApiInvocation}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2ApiInvocationTests {

    @Test
    void proceed_NoArg_Normal() throws OAuth2Exception {
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
        Iterator<OAuth2ApiInterceptor> interceptorIterator = Arrays.asList(interceptor).iterator();
        OAuth2ApiInvocation instance =
                new OAuth2ApiInvocation(platformApplication, api, interceptorIterator, null);
        assertNull(instance.getInput());
        Object result = instance.proceed();
        assertSame(inputAfterIntercepted, instance.getInput());
        assertSame(inputAfterIntercepted, result);
        verify(api, times(1)).execute(any());
        verify(interceptor, times(1)).invoke(any());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void OAuth2ApiInvocation_OAuth2PlatformApplication$OAuth2Api$Iterator$Object_Normal() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        Iterator<OAuth2ApiInterceptor> interceptorIterator = Arrays.asList(interceptor).iterator();
        Object input = new Object();
        OAuth2ApiInvocation instance =
                new OAuth2ApiInvocation(platformApplication, api, interceptorIterator, input);
        assertSame(platformApplication, instance.getPlatformApplication());
        assertSame(api, instance.getApi());
        assertSame(input, instance.getInput());
        assertNotNull(instance.getContext());
    }

    @Test
    void OAuth2ApiInvocation_OAuth2PlatformApplication$OAuth2Api$Iterator$Object_NullPlatformName() {
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        Iterator<OAuth2ApiInterceptor> interceptorIterator = Arrays.asList(interceptor).iterator();
        Object input = new Object();
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocation(null, api, interceptorIterator, input);
        });
    }

    @Test
    void OAuth2ApiInvocation_OAuth2PlatformApplication$OAuth2Api$Iterator$Object_NullApi() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2ApiInterceptor interceptor = mock(OAuth2ApiInterceptor.class);
        Iterator<OAuth2ApiInterceptor> interceptorIterator = Arrays.asList(interceptor).iterator();
        Object input = new Object();
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocation(platformApplication, null, interceptorIterator, input);
        });
    }

    @Test
    void OAuth2ApiInvocation_OAuth2PlatformApplication$OAuth2Api$Iterator$Object_NullInterceptorIterator() {
        OAuth2PlatformApplication platformApplication = mock(OAuth2PlatformApplication.class);
        OAuth2Api<?, ?> api = spy(OAuth2Api.class);
        Object input = new Object();
        assertThrows(NullPointerException.class, () -> {
            // noinspection ConstantConditions
            new OAuth2ApiInvocation(platformApplication, api, null, input);
        });
    }

}

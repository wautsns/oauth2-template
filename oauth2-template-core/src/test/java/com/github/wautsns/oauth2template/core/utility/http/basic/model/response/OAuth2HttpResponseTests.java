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
package com.github.wautsns.oauth2template.core.utility.http.basic.model.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import com.github.wautsns.oauth2template.core.exception.OAuth2IOException;

import com.fasterxml.jackson.databind.node.NullNode;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link OAuth2HttpResponse}.
 *
 * @author wautsns
 * @since {{{SINCE_PLACEHOLDER}}}
 */
class OAuth2HttpResponseTests {

    private static InputStream toInputStream(String text) {
        return new ByteArrayInputStream(text.getBytes());
    }

    // ##################################################################################

    @Test
    void parseBodyAsString_NoArg_Normal() throws Exception {
        OAuth2HttpResponse instanceA = spy(OAuth2HttpResponse.class);
        doReturn(toInputStream("{\"name\":123123}")).when(instanceA).getBodyInputStream();
        assertEquals("{\"name\":123123}", instanceA.parseBodyAsString());
        assertEquals("{\"name\":123123}", instanceA.parseBodyAsString());
        OAuth2HttpResponse instanceB = spy(OAuth2HttpResponse.class);
        doReturn(null).when(instanceB).getBodyInputStream();
        assertNull(instanceB.parseBodyAsString());
        assertNull(instanceB.parseBodyAsString());
    }

    @Test
    void parseBodyAsString_NoArg_ThrowOAuth2IOException() throws Exception {
        OAuth2HttpResponse instance = spy(OAuth2HttpResponse.class);
        doThrow(IOException.class).when(instance).getBodyInputStream();
        assertThrows(OAuth2IOException.class, instance::parseBodyAsString);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void parseBodyAsJson_NoArg_Normal() throws Exception {
        OAuth2HttpResponse instanceA = spy(OAuth2HttpResponse.class);
        doReturn(toInputStream("{\"name\":\"value\"}")).when(instanceA).getBodyInputStream();
        assertEquals("value", instanceA.parseBodyAsJson().path("name").asText(null));
        assertEquals("value", instanceA.parseBodyAsJson().path("name").asText(null));
        OAuth2HttpResponse instanceB = spy(OAuth2HttpResponse.class);
        doReturn(null).when(instanceB).getBodyInputStream();
        assertEquals(NullNode.getInstance(), instanceB.parseBodyAsJson());
        assertEquals(NullNode.getInstance(), instanceB.parseBodyAsJson());
    }

    @Test
    void parseBodyAsJson_NoArg_ThrowOAuth2IOException() throws Exception {
        OAuth2HttpResponse instance = spy(OAuth2HttpResponse.class);
        doThrow(IOException.class).when(instance).getBodyInputStream();
        assertThrows(OAuth2IOException.class, instance::parseBodyAsJson);
    }

    @Test
    void parseBodyAsJson_NoArg_IllegalJsonBody() throws Exception {
        OAuth2HttpResponse instance = spy(OAuth2HttpResponse.class);
        doReturn(toInputStream("{{{")).when(instance).getBodyInputStream();
        assertThrows(OAuth2IOException.class, instance::parseBodyAsJson);
    }

}

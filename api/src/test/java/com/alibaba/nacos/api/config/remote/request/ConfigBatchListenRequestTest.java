/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.api.config.remote.request;

import com.alibaba.nacos.api.common.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigBatchListenRequestTest extends BasedConfigRequestTest {
    
    @Override
    @Test
    public void testSerialize() throws JsonProcessingException {
        ConfigBatchListenRequest configBatchListenRequest = new ConfigBatchListenRequest();
        configBatchListenRequest.putAllHeader(HEADERS);
        configBatchListenRequest.addConfigListenContext(GROUP, DATA_ID, TENANT, MD5);
        final String requestId = injectRequestUuId(configBatchListenRequest);
        String json = mapper.writeValueAsString(configBatchListenRequest);
        assertTrue(json.contains("\"listen\":" + "true"));
        assertTrue(json.contains(
                "\"configListenContexts\":[{\"dataId\":\"test_data\",\"group\":\"group\",\"md5\":\"test_MD5\",\"tenant\":\"test_tenant\"}]"));
        assertTrue(json.contains("\"module\":\"" + Constants.Config.CONFIG_MODULE));
        assertTrue(json.contains("\"requestId\":\"" + requestId));
    }
    
    @Override
    @Test
    public void testDeserialize() throws JsonProcessingException {
        String json = "{\"headers\":{\"header1\":\"test_header1\"},\"listen\":true,"
                + "\"configListenContexts\":[{\"group\":\"group\",\"md5\":\"test_MD5\","
                + "\"dataId\":\"test_data\",\"tenant\":\"test_tenant\"}],\"module\":\"config\"}";
        ConfigBatchListenRequest actual = mapper.readValue(json, ConfigBatchListenRequest.class);
        assertEquals(true, actual.isListen());
        assertEquals(Constants.Config.CONFIG_MODULE, actual.getModule());
        assertEquals(HEADER_VALUE, actual.getHeader(HEADER_KEY));
        assertEquals(1, actual.getConfigListenContexts().size());
    }
    
    @Test
    void testConfigListenContextToString() {
        ConfigBatchListenRequest configBatchListenRequest = new ConfigBatchListenRequest();
        configBatchListenRequest.addConfigListenContext(GROUP, DATA_ID, TENANT, MD5);
        assertEquals("ConfigListenContext{group='group', md5='test_MD5', dataId='test_data', tenant='test_tenant'}",
                configBatchListenRequest.getConfigListenContexts().get(0).toString());
    }
}

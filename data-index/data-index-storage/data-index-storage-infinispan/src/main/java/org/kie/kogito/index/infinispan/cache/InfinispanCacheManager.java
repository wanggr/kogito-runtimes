/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.infinispan.cache;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.DataFormat;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.dataconversion.MediaType;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.kie.kogito.index.cache.CacheService;
import org.kie.kogito.index.model.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class InfinispanCacheManager implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanCacheManager.class);
    private static final String PROCESS_INSTANCES_CACHE = "processinstances";

    private DataFormat jsonDataFormat = DataFormat.builder().valueType(MediaType.APPLICATION_JSON).valueMarshaller(new JsonDataFormatMarshaller()).build();

    @Inject
    @ConfigProperty(name = "kogito.cache.domain.template", defaultValue = "kogito-template")
    String domainCacheTemplate;

    @Inject
    RemoteCacheManager manager;

    @PostConstruct
    public void init() {
        manager.start();
    }

    @PreDestroy
    public void destroy() {
        manager.stop();
        try {
            manager.close();
        } catch (IOException ex) {
            LOGGER.warn("Error trying to close Infinispan remote cache manager", ex);
        }
    }

    @Override
    public Map<String, ProcessInstance> getProcessInstancesCache() {
        return manager.administration().getOrCreateCache(PROCESS_INSTANCES_CACHE, domainCacheTemplate);
    }

    @Override
    public Map<String, JsonObject> getProcessInstancesCacheAsJson() {
        return manager.administration().getOrCreateCache(PROCESS_INSTANCES_CACHE, domainCacheTemplate).withDataFormat(jsonDataFormat);
    }

    @Override
    public Map<String, String> getProtobufCache() {
        return manager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
    }

    @Override
    public Map<String, String> getProcessIdModelCache() {
        return manager.administration().getOrCreateCache("processidmodel", (String) null);
    }

    @Override
    public Map<String, JsonObject> getDomainModelCache(String processId) {
        return manager.administration().getOrCreateCache(processId + "_domain", domainCacheTemplate).withDataFormat(jsonDataFormat);
    }
}
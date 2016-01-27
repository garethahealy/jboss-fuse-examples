/*
 * #%L
 * ws-security-https-cxf-client
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
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
 * #L%
 */
package com.garethahealy.wssecurity.https.cxf.client.resolvers;

import java.util.HashMap;
import java.util.Map;

import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class CachedResolver<T> implements Resolver<T> {

    private Map<String, T> cached;
    private WsEndpointConfiguration<?> config;
    private JaxWsProxyFactoryBean factory;

    public CachedResolver(WsEndpointConfiguration<?> config, JaxWsProxyFactoryBean factory) {
        this.cached = new HashMap<String, T>();
        this.config = config;
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized T createEndpoint() {
        T port = null;

        //NOTE: We want to cache endpoints, but since endpoints are configured with keystores for
        //signatures we need to cache via a key such as below
        String url = config.getWsAddress();
        String certAlias = config.getCertifactionAlias();
        String signatureKeystore = config.getSignatureKeystoreFilename();
        String key = String.format("%s_%s_%s", url, certAlias, signatureKeystore);

        if (cached.containsKey(key)) {
            port = cached.get(key);
        } else {
            port = (T)factory.create();

            cached.put(key, port);
        }

        return port;
    }
}

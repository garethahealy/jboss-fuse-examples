package com.garethahealy.wssecurity.https.cxf.client.resolvers;

import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.util.HashMap;
import java.util.Map;

public class CachedResolver<T> implements Resolver<T> {

    private Map<String, T> cached = null;
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

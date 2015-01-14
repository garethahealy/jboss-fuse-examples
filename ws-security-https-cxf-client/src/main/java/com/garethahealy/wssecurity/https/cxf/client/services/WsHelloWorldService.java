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
package com.garethahealy.wssecurity.https.cxf.client.services;

import java.io.InvalidObjectException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.config.WsMapper;
import com.garethahealy.wssecurity.https.cxf.client.decorators.HTTPSWsSignatureEndpointDecorator;
import com.garethahealy.wssecurity.https.cxf.client.javaxws.BareBones;
import com.garethahealy.wssecurity.https.cxf.client.javaxws.DynamicClient;
import com.garethahealy.wssecurity.https.cxf.client.resolvers.CachedResolver;
import com.garethahealy.wssecurity.https.cxf.client.resolvers.Resolver;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class WsHelloWorldService {

    private WsEndpointConfiguration<HelloWorldEndpoint> config;
    private boolean isCxfBeanFactory;

    private String wsdl = "file:/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/ws-security-https-cxf-wsdl-helloworld/src/main/resources/wsdl/helloworld.wsdl";

    public WsHelloWorldService(WsEndpointConfiguration<HelloWorldEndpoint> config, boolean isCxfBeanFactory) {
        this.config = config;
        this.isCxfBeanFactory = isCxfBeanFactory;
    }

    private HelloWorldEndpoint resolveEndpoint() {
        Resolver<HelloWorldEndpoint> resolver = new CachedResolver<HelloWorldEndpoint>(config, new HTTPSWsSignatureEndpointDecorator(config));
        return resolver.createEndpoint();
    }

    private HelloWorldEndpoint resolveByBareBones() throws InvalidObjectException {
        BareBones bareBones = new BareBones();
        return bareBones.getEndpoint(config);
    }

    public HelloWorldResponse sayHello(HelloWorldRequest request) throws InvalidObjectException {
        HelloWorldEndpoint endpoint = isCxfBeanFactory ? resolveEndpoint() : resolveByBareBones();
        return endpoint.sayHello(request);
    }

    public Object[] reflectionExample() throws Exception {
        Map<String, Object> methodNameToValue = new HashMap<String, Object>();
        methodNameToValue.put("setHello", "Hello? I am Mr Reflection!");

        WsMapper mapper = new WsMapper();
        mapper.setOperationName("SayHello");
        mapper.setClassName(HelloWorldRequest.class.getCanonicalName());
        mapper.setMethodNameToValue(methodNameToValue);

        DynamicClient dynamicClient = DynamicClient.newInstance();
        Client client = dynamicClient.createClient(wsdl);
        Object request = dynamicClient.getRequest(mapper);

        return dynamicClient.callMethod(client, mapper.getOperationName(), request);
    }
}

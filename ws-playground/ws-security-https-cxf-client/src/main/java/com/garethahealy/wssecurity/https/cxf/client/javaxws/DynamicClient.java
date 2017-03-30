/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF Client
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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
package com.garethahealy.wssecurity.https.cxf.client.javaxws;

import java.util.Map;

import com.garethahealy.wssecurity.https.cxf.client.config.WsMapper;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class DynamicClient extends JaxWsDynamicClientFactory {

    //http://cxf.apache.org/docs/dynamic-clients.html

    protected DynamicClient(Bus bus) {
        super(bus);
    }

    public Object getRequest(WsMapper mapper) throws Exception {
        Object request = ConstructorUtils.invokeConstructor(Class.forName(mapper.getClassName()));

        for (Map.Entry<String, Object> current : mapper.getMethodNameToValue().entrySet()) {
            MethodUtils.invokeMethod(request, current.getKey(), current.getValue());
        }

        return request;
    }

    public Object[] callMethod(Client client, String operationName, Object request) throws Exception {
        return client.invoke(operationName, request);
    }

    //public Object[] flattenResponse(WsMapper mapper, Object[] response) {
    //    return null;
    //}

    public static DynamicClient newInstance() {
        Bus bus = CXFBusFactory.getThreadDefaultBus();
        return new DynamicClient(bus);
    }
}

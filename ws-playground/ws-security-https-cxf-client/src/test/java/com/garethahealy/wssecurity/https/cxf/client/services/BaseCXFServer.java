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
package com.garethahealy.wssecurity.https.cxf.client.services;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.wssecurity.https.cxf.client.FakeHelloWorldEndpointService;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class BaseCXFServer {

    private Server serverJetty;

    protected void startServer() throws Exception {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(HelloWorldEndpoint.class);
        factory.setAddress("http://0.0.0.0:9001/cxf/helloWorldService");
        factory.setServiceBean(new FakeHelloWorldEndpointService());

        serverJetty = factory.create();
        serverJetty.start();
    }

    protected void stopServer() {
        serverJetty.stop();
        serverJetty.destroy();
        serverJetty = null;
    }
}

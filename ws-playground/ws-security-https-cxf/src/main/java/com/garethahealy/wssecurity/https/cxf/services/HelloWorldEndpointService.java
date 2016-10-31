/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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
package com.garethahealy.wssecurity.https.cxf.services;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.impl.HelloWorldResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldEndpointService implements HelloWorldEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldEndpointService.class);

    private HelloWorldResponseBuilder builder;

    public HelloWorldEndpointService(HelloWorldResponseBuilder builder) {
        this.builder = builder;
    }

    public HelloWorldResponse sayHello(HelloWorldRequest body) {
        LOG.info("HelloWorldEndpointService received");
        return builder.getResponse(body);
    }
}

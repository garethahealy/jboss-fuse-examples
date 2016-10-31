/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF Client
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
package com.garethahealy.wssecurity.https.cxf.client.services;

import com.garethahealy.helloworld.HelloWorldResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WsHelloWorldServiceDynamicTest extends BaseCXFServer {

    @Before
    public void setup() throws Exception {
        startServer();
    }

    @After
    public void teardown() {
        stopServer();
    }

    @Test
    public void can() throws Exception {
        WsHelloWorldService service = new WsHelloWorldService(null, false);
        Object[] result = service.reflectionExample();

        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(1), new Integer(result.length));
        Assert.assertTrue(result[0] instanceof HelloWorldResponse);
        Assert.assertNotNull(((HelloWorldResponse)result[0]).getGoodbye());
    }
}

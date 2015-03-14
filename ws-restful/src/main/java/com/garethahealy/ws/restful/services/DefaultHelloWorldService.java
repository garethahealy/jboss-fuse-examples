/*
 * #%L
 * ws-restful
 * %%
 * Copyright (C) 2013 - 2015 Gareth Healy
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
package com.garethahealy.ws.restful.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.ws.restful.builders.HelloWorldResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/default/")
public class DefaultHelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHelloWorldService.class);

    private HelloWorldResponseBuilder builder;

    public DefaultHelloWorldService() {
        this.builder = new HelloWorldResponseBuilder();
    }

    @GET
    @Path("/sayHello/{name}")
    @Produces("application/xml")
    public HelloWorldResponse sayHello(@PathParam("name") String name) {
        LOG.debug("sayHello to '{}'", name);

        HelloWorldRequest in = new HelloWorldRequest();
        in.setHello(name);

        return builder.getResponse(in);
    }
}

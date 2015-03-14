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

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.ws.restful.builders.HelloWorldResponseBuilder;

@Path("/default/")
public class DefaultHelloWorldService implements HelloWorldEndpoint {

    private HelloWorldResponseBuilder builder;

    public DefaultHelloWorldService() {

    }

    public DefaultHelloWorldService(HelloWorldResponseBuilder builder) {
        this.builder = builder;
    }

    @GET
    @Path("/sayHello/")
    @Produces("application/xml")
    @Override
    public HelloWorldResponse sayHello(HelloWorldRequest in) {
        return builder.getResponse(in);
    }
}

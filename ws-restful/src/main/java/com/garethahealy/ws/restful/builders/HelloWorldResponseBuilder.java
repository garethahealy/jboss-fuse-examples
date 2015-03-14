/*
 * #%L
 * ws-security-https-cxf
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
package com.garethahealy.ws.restful.builders;

import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;

public class HelloWorldResponseBuilder {

    public HelloWorldResponse getResponse(HelloWorldRequest body) {
        HelloWorldResponse response = new HelloWorldResponse();
        response.setGoodbye(body.getHello() + " # Ahhh...what a shame to see you leave :(");

        return response;
    }
}

package com.garethahealy.wssecurity.https.cxf.impl;

import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;

public class HelloWorldResponseBuilder {

        public HelloWorldResponse getResponse(HelloWorldRequest body) {
                HelloWorldResponse response = new HelloWorldResponse();
                response.setGoodbye(body.getHello() + " # Ahhh...what a shame to see you leave :(");

                return response;
        }
}

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

package com.garethahealy.wssecurity.https.cxf.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.helloworld.HelloWorldEndpoint;

import com.garethahealy.wssecurity.https.cxf.impl.HelloWorldResponseBuilder;

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

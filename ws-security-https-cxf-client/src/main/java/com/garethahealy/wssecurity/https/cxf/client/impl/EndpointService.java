package com.garethahealy.wssecurity.https.cxf.client.impl;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;

public class EndpointService {

	public void sayHello() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		EndpointConfig config = new EndpointConfig();
		
		HelloWorldEndpoint endpoint = config.getEndpoint(true);
		endpoint.sayHello(request);
	}
}

package com.garethahealy.wssecurity.https.cxf.client.services;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.decorators.HTTPSWsSignatureEndpointDecorator;

public class WsHelloWorldService {

	private WsEndpointConfiguration<HelloWorldEndpoint> config;
	
	public WsHelloWorldService(WsEndpointConfiguration<HelloWorldEndpoint> config) {
		this.config = config;
	}
	
	private HelloWorldEndpoint resolveEndpoint() {
		HTTPSWsSignatureEndpointDecorator wsEndpointDecorator = new HTTPSWsSignatureEndpointDecorator(config);
		return wsEndpointDecorator.createTyped();
	}
	
	public HelloWorldResponse sayHello(HelloWorldRequest request) {
		HelloWorldEndpoint endpoint = resolveEndpoint();
		return endpoint.sayHello(request);
	}
}

package com.garethahealy.wssecurity.https.cxf.client.impl;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.DefaultWsTLSClientDecorator;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointFactory;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsInterceptorFactory;

public class WsHelloWorldService {

	private WsEndpointConfiguration<HelloWorldEndpoint> config;
	
	public WsHelloWorldService(WsEndpointConfiguration<HelloWorldEndpoint> config) {
		this.config = config;
	}
	
	private HelloWorldEndpoint resolveEndpoint() {
		WsInterceptorFactory wsInterceptorFactory = new WsInterceptorFactory();
		DefaultWsTLSClientDecorator tlsClientdecorator = new DefaultWsTLSClientDecorator();
		WsEndpointFactory endpointFactory = new WsEndpointFactory(wsInterceptorFactory, tlsClientdecorator);

		return endpointFactory.getEndpoint(this.config);
	}
	
	public HelloWorldResponse sayHello() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		HelloWorldEndpoint endpoint = resolveEndpoint();
		return endpoint.sayHello(request);
	}
}

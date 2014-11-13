package com.garethahealy.wssecurity.https.cxf.client.services;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.decorators.HTTPSWsSignatureEndpointDecorator;
import com.garethahealy.wssecurity.https.cxf.client.javaxws.BareBones;
import com.garethahealy.wssecurity.https.cxf.client.resolvers.CachedResolver;
import com.garethahealy.wssecurity.https.cxf.client.resolvers.Resolver;

public class WsHelloWorldService {

	private WsEndpointConfiguration<HelloWorldEndpoint> config;
	private boolean isCxfBeanFactory;
	
	public WsHelloWorldService(WsEndpointConfiguration<HelloWorldEndpoint> config, boolean isCxfBeanFactory) {
		this.config = config;
		this.isCxfBeanFactory = isCxfBeanFactory;
	}
	
	private HelloWorldEndpoint resolveEndpoint() {
		Resolver<HelloWorldEndpoint> resolver = new CachedResolver<HelloWorldEndpoint>(config, new HTTPSWsSignatureEndpointDecorator(config));
		return resolver.createEndpoint();
	}
	
	private HelloWorldEndpoint resolveByBareBones() {
		BareBones bareBones = new BareBones();
		return bareBones.getEndpoint(config);
	}
	
	public HelloWorldResponse sayHello(HelloWorldRequest request) {
		HelloWorldEndpoint endpoint = isCxfBeanFactory ? resolveEndpoint() : resolveByBareBones();
		return endpoint.sayHello(request);
	}
}

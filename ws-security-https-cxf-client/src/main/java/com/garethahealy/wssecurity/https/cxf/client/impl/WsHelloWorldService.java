package com.garethahealy.wssecurity.https.cxf.client.impl;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.DefaultWsTLSClientDecorator;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointFactory;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsInterceptorFactory;

public class WsHelloWorldService {

	public void sayHello() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsInterceptorFactory wsInterceptorFactory = new WsInterceptorFactory();
		DefaultWsTLSClientDecorator tlsClientdecorator = new DefaultWsTLSClientDecorator();
		WsEndpointFactory endpointFactory = new WsEndpointFactory(wsInterceptorFactory, tlsClientdecorator);

		HelloWorldEndpoint endpoint = endpointFactory.getEndpoint(getConfig());
		endpoint.sayHello(request);
	}
	
	private WsEndpointConfiguration<HelloWorldEndpoint> getConfig() {
		WsEndpointConfiguration<HelloWorldEndpoint> config = new WsEndpointConfiguration<HelloWorldEndpoint>();
		config.setCxfDebug(true);
		config.setCertifactionAlias("clientx509v1");
		config.setWsAddress("https://0.0.0.0:9001/cxf/helloWorldService");
		config.setServiceClass(HelloWorldEndpoint.class);
		config.setPathToKeystore("/NotBackedUp/jboss-studio-workspace/jboss-fuse-examples/ws-security-https-cxf-client/src/main/resources/keystore");
		config.setPathToTruststore("/NotBackedUp/jboss-studio-workspace/jboss-fuse-examples/ws-security-https-cxf-client/src/main/resources/keystore");
		config.setKeystoreFilename("client-keystore.jks");
		config.setTruststoreFilename("client-truststore.jks");
		config.setKeystorePassword("storepassword");
		config.setTruststorePassword("storepassword");
		config.setKeyManagerPassword("storepassword");
		
		return config;
	}
}

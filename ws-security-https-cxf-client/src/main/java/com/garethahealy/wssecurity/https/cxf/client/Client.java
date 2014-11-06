package com.garethahealy.wssecurity.https.cxf.client;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.impl.WsHelloWorldService;

public class Client {

	public static void main(String [] args) {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsHelloWorldService service = new WsHelloWorldService(getConfig());
		service.sayHello(request);
	}
	
	private static WsEndpointConfiguration<HelloWorldEndpoint> getConfig() {
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
		config.setSignaturePropFile("ws-signature/Client_Sign.properties");
		config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.impl.UTPasswordCallback");
		
		return config;
	}
}

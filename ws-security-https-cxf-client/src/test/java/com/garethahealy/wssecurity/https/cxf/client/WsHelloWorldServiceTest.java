package com.garethahealy.wssecurity.https.cxf.client;

import org.junit.Assert;
import org.junit.Test;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.impl.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.impl.WsHelloWorldService;

public class WsHelloWorldServiceTest {

	private static WsEndpointConfiguration<HelloWorldEndpoint> getDefaultConfig() {
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
	
	@Test
	public void can_get_response() {
		WsHelloWorldService service = new WsHelloWorldService(getDefaultConfig());
		HelloWorldResponse response = service.sayHello();
		
		Assert.assertNotNull(response);
	}
}

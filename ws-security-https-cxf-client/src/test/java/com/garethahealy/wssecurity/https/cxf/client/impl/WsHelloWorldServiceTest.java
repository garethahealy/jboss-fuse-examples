package com.garethahealy.wssecurity.https.cxf.client.impl;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Assert;
import org.junit.Test;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
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
		config.setSignaturePropFile("ws-signature/Client_Sign.properties");
		config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.impl.UTPasswordCallback");
		
		return config;
	}
	
	@Test
	public void can_get_response() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsHelloWorldService service = new WsHelloWorldService(getDefaultConfig());
		HelloWorldResponse response = service.sayHello(request);
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getGoodbye());
		Assert.assertTrue(response.getGoodbye().length() > 0);
		Assert.assertTrue(response.getGoodbye().startsWith(request.getHello()));
	}
	
	
	@Test(expected=SOAPFaultException.class)
	public void throws_exception_due_to_missing_client_alias() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setCertifactionAlias("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=WebServiceException.class)
	public void throws_exception_due_to_missing_keystore_password() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setKeystorePassword("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=WebServiceException.class)
	public void throws_exception_due_to_missing_truststore_password() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setTruststorePassword("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=WebServiceException.class)
	public void throws_exception_due_to_missing_truststore_file() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setTruststoreFilename("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=WebServiceException.class)
	public void throws_exception_due_to_missing_keystore_file() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setKeystoreFilename("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=SOAPFaultException.class)
	public void throws_exception_due_to_missing_signature_props() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setSignaturePropFile("");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=SOAPFaultException.class)
	public void throws_exception_due_wrong_signature_cert() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setCertifactionAlias("mykey");
		config.setSignaturePropFile("ws-signature/wrong-Client_Sign.properties");
		config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.impl.FakeUTPasswordCallback");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
	
	@Test(expected=WebServiceException.class)
	public void throws_exception_due_wrong_https_cert() {
		HelloWorldRequest request = new HelloWorldRequest();
		request.setHello("bob");
		
		WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
		config.setKeystoreFilename("wrong-keystore.jks");
		config.setTruststoreFilename("wrong-truststore.jks");
		config.setKeystorePassword("password");
		config.setTruststorePassword("password");
		
		WsHelloWorldService service = new WsHelloWorldService(config);
		service.sayHello(request);
	}
}

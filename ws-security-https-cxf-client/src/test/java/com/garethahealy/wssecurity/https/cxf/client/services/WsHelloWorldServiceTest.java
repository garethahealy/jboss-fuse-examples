/*
 * #%L
 * ws-security-https-cxf-client
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.wssecurity.https.cxf.client.services;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import com.garethahealy.helloworld.HelloWorldEndpoint;
import com.garethahealy.helloworld.HelloWorldRequest;
import com.garethahealy.helloworld.HelloWorldResponse;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

import org.junit.Assert;
import org.junit.Test;

public class WsHelloWorldServiceTest {

    private WsEndpointConfiguration<HelloWorldEndpoint> getDefaultConfig() {
        WsEndpointConfiguration<HelloWorldEndpoint> config = new WsEndpointConfiguration<HelloWorldEndpoint>();
        config.setCxfDebug(true);
        config.setCertifactionAlias("clientx509v1");
        config.setWsAddress("https://0.0.0.0:9001/cxf/helloWorldService");
        config.setServiceClass(HelloWorldEndpoint.class);
        config.setPathToKeystore("/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/ws-security-https-cxf-client/src/main/resources/keystore");
        config.setPathToTruststore("/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/ws-security-https-cxf-client/src/main/resources/keystore");
        config.setKeystoreFilename("client-keystore.jks");
        config.setTruststoreFilename("client-truststore.jks");
        config.setKeystorePassword("storepassword");
        config.setTruststorePassword("storepassword");
        config.setKeyManagerPassword("storepassword");
        config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.config.UTPasswordCallback");
        config.setSignatureKeystoreFilename("keystore/client-keystore.jks");
        config.setSignatureKeystorePassword("storepassword");

        return config;
    }

    private boolean isCxfBeanFactory() {
        return true;
    }

    @Test
    public void canGetResponseDefaultSignatureCert() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsHelloWorldService service = new WsHelloWorldService(getDefaultConfig(), isCxfBeanFactory());
        HelloWorldResponse response = service.sayHello(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getGoodbye());
        Assert.assertTrue(response.getGoodbye().length() > 0);
        Assert.assertTrue(response.getGoodbye().startsWith(request.getHello()));
    }

    @Test
    public void canGetResponseAnotherSignatureCert() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setCertifactionAlias("garethskey");
        config.setSignatureKeystoreFilename("keystore/another-client-keystore.jks");
        config.setSignatureKeystorePassword("password");
        config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.config.FakeUTPasswordCallback");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        HelloWorldResponse response = service.sayHello(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getGoodbye());
        Assert.assertTrue(response.getGoodbye().length() > 0);
        Assert.assertTrue(response.getGoodbye().startsWith(request.getHello()));
    }

    @Test(expected = WebServiceException.class)
    public void callFailsOverHttp() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setWsAddress("http://0.0.0.0:9001/cxf/helloWorldService");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        HelloWorldResponse response = service.sayHello(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getGoodbye());
        Assert.assertTrue(response.getGoodbye().length() > 0);
        Assert.assertTrue(response.getGoodbye().startsWith(request.getHello()));
    }

    @Test(expected = SOAPFaultException.class)
    public void throwsExceptionDueToMissingClientAlias() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setCertifactionAlias("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = WebServiceException.class)
    public void throwsExceptionDueToMissingKeystorePassword() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setKeystorePassword("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = WebServiceException.class)
    public void throwsExceptionDueToMissingTruststorePassword() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setTruststorePassword("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = WebServiceException.class)
    public void throwsExceptionDueToMissingTruststoreFile() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setTruststoreFilename("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = WebServiceException.class)
    public void throwsExceptionDueToMissingKeystoreFile() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setKeystoreFilename("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = SOAPFaultException.class)
    public void throwsExceptionDueToMissingSignatureProps() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setSignatureKeystoreFilename("");
        config.setSignatureKeystorePassword("");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = SOAPFaultException.class)
    public void throwsExceptionDueToWrongSignatureCert() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setCertifactionAlias("mykey");
        config.setSignatureKeystoreFilename("keystore/wrong-keystore.jks");
        config.setSignatureKeystorePassword("password");
        config.setPasswordCallbackClass("com.garethahealy.wssecurity.https.cxf.client.config.FakeUTPasswordCallback");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }

    @Test(expected = WebServiceException.class)
    public void throwsExceptionDueToWrongHttpsCert() {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        WsEndpointConfiguration<HelloWorldEndpoint> config = getDefaultConfig();
        config.setKeystoreFilename("wrong-keystore.jks");
        config.setTruststoreFilename("wrong-truststore.jks");
        config.setKeystorePassword("password");
        config.setTruststorePassword("password");

        WsHelloWorldService service = new WsHelloWorldService(config, isCxfBeanFactory());
        service.sayHello(request);
    }
}

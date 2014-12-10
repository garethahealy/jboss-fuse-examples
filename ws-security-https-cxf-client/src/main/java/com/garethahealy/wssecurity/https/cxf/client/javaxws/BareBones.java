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
package com.garethahealy.wssecurity.https.cxf.client.javaxws;

import com.garethahealy.helloworld.HelloWorldEndpointService;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.decorators.HTTPSWsSignatureEndpointDecorator;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BareBones {

	/*
     * NOTE: This class is a barebones interaction with javax.xml.ws.Service.
	 * 		 Its much better to use the JaxWsProxyFactoryBean examples!
	 * 		 Please dont go down this route!
	 */

    @SuppressWarnings("rawtypes")
    public <T> T getEndpoint(WsEndpointConfiguration<?> config) {
        T port = null;
        try {
            Class classImpl = Class.forName(HelloWorldEndpointService.class.getCanonicalName());
            Service service = createService(classImpl,
                                            "file:/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/ws-security-https-cxf-wsdl-helloworld/src/main/resources/wsdl/helloworld.wsdl");

            addExtraHandler(service);

            port = createEndpoint(classImpl, service);

            //Update URL to be what comes from config
            BindingProvider bp = (BindingProvider)port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getWsAddress());

            //NOTE: We are just cheating here, as we've already done the code for HTTPS and WS-Security
            //		am just gonna re-use these methods
            HTTPSWsSignatureEndpointDecorator decorator = new HTTPSWsSignatureEndpointDecorator(config);

            Client client = ClientProxy.getClient(port);
            Endpoint endpoint = client.getEndpoint();
            endpoint.getInInterceptors().addAll(decorator.getLogInInterceptors());
            endpoint.getOutInterceptors().addAll(decorator.getLogOutInterceptors());
            endpoint.getOutInterceptors().add(decorator.getWSS4JOutInterceptor());

            decorator.configureSSLOnTheClient(client);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return port;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Service createService(Class classImpl, String url)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException, NoSuchMethodException,
        SecurityException {
        Constructor constr = classImpl.getConstructor(URL.class);

        return (Service)constr.newInstance(new URL(url));
    }

    private void addExtraHandler(Service service) {
        service.setHandlerResolver(new HandlerResolver() {

            @Override
            public List<Handler> getHandlerChain(PortInfo portInfo) {
                List<Handler> handlers = new ArrayList<Handler>();
                handlers.add(new HeaderHandler());
                return handlers;
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T createEndpoint(Class classImpl, Service service) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method found = null;
        for (Method meth : classImpl.getMethods()) {
            if (meth.getName().equalsIgnoreCase("getHelloWorldEndpoint") && meth.getParameterTypes().length == 0) {
                found = meth;
                break;
            }
        }

        if (found == null) {
            throw new NullPointerException("Port method not found on endpoint");
        }

        return (T)found.invoke(service);
    }
}

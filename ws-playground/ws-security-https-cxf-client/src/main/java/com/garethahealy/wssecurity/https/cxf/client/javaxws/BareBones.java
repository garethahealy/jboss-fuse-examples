/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Security HTTPS CXF Client
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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

import java.io.InvalidObjectException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import com.garethahealy.helloworld.HelloWorldEndpointService;
import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;
import com.garethahealy.wssecurity.https.cxf.client.decorators.HTTPSWsSignatureEndpointDecorator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BareBones {

    private static final Logger LOG = LoggerFactory.getLogger(BareBones.class);

    /*
     * NOTE: This class is a barebones interaction with javax.xml.ws.Service.
     *       Its much better to use the JaxWsProxyFactoryBean examples!
     *       Please dont go down this route!
     */

    private String wsdl = "file:/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/ws-security-https-cxf-wsdl-helloworld/src/main/resources/wsdl/helloworld.wsdl";

    @SuppressWarnings("rawtypes")
    public <T> T getEndpoint(WsEndpointConfiguration<?> config) throws InvalidObjectException {
        T port = null;

        Class classImpl = getClassForName(HelloWorldEndpointService.class.getCanonicalName());
        Service service = createService(classImpl, wsdl);

        addExtraHandler(service);

        port = createEndpoint(classImpl, service);

        //Update URL to be what comes from config
        BindingProvider bp = (BindingProvider)port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getWsAddress());

        //NOTE: We are just cheating here, as we've already done the code for HTTPS and WS-Security
        //      am just gonna re-use these methods
        HTTPSWsSignatureEndpointDecorator decorator = new HTTPSWsSignatureEndpointDecorator(config);

        Client client = ClientProxy.getClient(port);
        Endpoint endpoint = client.getEndpoint();
        endpoint.getInInterceptors().addAll(decorator.getLogInInterceptors());
        endpoint.getOutInterceptors().addAll(decorator.getLogOutInterceptors());
        endpoint.getOutInterceptors().add(decorator.getWSS4JOutInterceptor());

        decorator.configureSSLOnTheClient(client);

        return port;
    }

    private Class getClassForName(String canonicalName) {
        Class answer = null;
        try {
            answer = Class.forName(canonicalName);
        } catch (ClassNotFoundException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return answer;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Service createService(Class classImpl, String url) {
        Service answer = null;

        try {
            Constructor constr = classImpl.getConstructor(URL.class);
            answer = (Service)constr.newInstance(new URL(url));
        } catch (NoSuchMethodException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (SecurityException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (MalformedURLException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (InstantiationException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (IllegalAccessException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (IllegalArgumentException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (InvocationTargetException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return answer;
    }

    private void addExtraHandler(Service service) {
        service.setHandlerResolver(new CustomHandlerResolver());
    }

    private static class CustomHandlerResolver implements HandlerResolver {
        @Override
        public List<Handler> getHandlerChain(PortInfo portInfo) {
            List<Handler> handlers = new ArrayList<Handler>();
            handlers.add(new HeaderHandler());
            return handlers;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T createEndpoint(Class classImpl, Service service) throws InvalidObjectException {
        Method found = null;
        for (Method meth : classImpl.getMethods()) {
            if (meth.getName().equalsIgnoreCase("getHelloWorldEndpoint") && meth.getParameterTypes().length == 0) {
                found = meth;
                break;
            }
        }

        if (found == null) {
            throw new InvalidObjectException("Port method not found on endpoint");
        }

        T endpoint = null;
        try {
            endpoint = (T)found.invoke(service);
        } catch (IllegalAccessException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (IllegalArgumentException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (InvocationTargetException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return endpoint;
    }
}

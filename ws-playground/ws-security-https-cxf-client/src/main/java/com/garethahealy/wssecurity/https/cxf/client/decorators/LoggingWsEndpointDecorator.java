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
package com.garethahealy.wssecurity.https.cxf.client.decorators;

import java.util.ArrayList;
import java.util.List;

import com.garethahealy.wssecurity.https.cxf.client.config.WsEndpointConfiguration;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;

public class LoggingWsEndpointDecorator extends JaxWsProxyFactoryBean {

    protected WsEndpointConfiguration<?> config;

    public LoggingWsEndpointDecorator(WsEndpointConfiguration<?> config) {
        this.config = config;
    }

    @Override
    public synchronized Object create() {
        this.getInInterceptors().addAll(getLogInInterceptors());
        this.getOutInterceptors().addAll(getLogOutInterceptors());
        this.setServiceClass(config.getServiceClass());
        this.setAddress(config.getWsAddress());

        return super.create();
    }

    public List<Interceptor<? extends Message>> getLogInInterceptors() {
        List<Interceptor<? extends Message>> inInterceptors = new ArrayList<Interceptor<? extends Message>>();
        if (config.isCxfDebug()) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            loggingInInterceptor.setPrettyLogging(true);

            inInterceptors.add(loggingInInterceptor);
        }

        return inInterceptors;
    }

    public List<Interceptor<? extends Message>> getLogOutInterceptors() {
        List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
        if (config.isCxfDebug()) {
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            loggingOutInterceptor.setPrettyLogging(true);

            outInterceptors.add(loggingOutInterceptor);
        }

        return outInterceptors;
    }
}

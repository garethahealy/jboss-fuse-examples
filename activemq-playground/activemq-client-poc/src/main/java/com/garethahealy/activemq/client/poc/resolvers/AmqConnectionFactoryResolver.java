/*
 * #%L
 * activemq-client-poc
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
package com.garethahealy.activemq.client.poc.resolvers;

import javax.jms.ConnectionFactory;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AmqConnectionFactoryResolver implements ConnectionFactoryResolver {

    private BrokerConfiguration brokerConfiguration;
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    public AmqConnectionFactoryResolver(BrokerConfiguration brokerConfiguration) {
        this.brokerConfiguration = brokerConfiguration;
    }

    private void init() {
        activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerConfiguration.getBrokerURL());
        activeMQConnectionFactory.setUserName(brokerConfiguration.getUsername());
        activeMQConnectionFactory.setPassword(brokerConfiguration.getPassword());
    }

    public ConnectionFactory start() {
        if (activeMQConnectionFactory == null) {
            init();
        }

        return activeMQConnectionFactory;
    }

    public void stop() {
        activeMQConnectionFactory = null;
    }
}

/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: ActiveMQ Playground :: Client POC
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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
package com.garethahealy.activemq.client.poc.producers.retryable;

import com.garethahealy.activemq.client.poc.callbacks.DefaultCallbackHandler;
import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.BaseBroker;
import com.garethahealy.activemq.client.poc.producers.CallbackableRetryableAmqProducer;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.services.MessageService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageServiceBrokerDownAfterQueueCreatedTest extends BaseBroker {

    private Producer getRetryableAmqProducerWithDownBroker() {
        RetryConfiguration retryConfiguration = new RetryConfiguration();
        BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
        connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

        DefaultCallbackHandler defaultCallbackHandler = new DefaultCallbackHandler() {
            @Override
            public void createQueue() {
                try {
                    stopBroker();
                } catch (Exception ex) {
                }
            }
        };

        return new CallbackableRetryableAmqProducer(defaultCallbackHandler, retryConfiguration, brokerConfiguration, connectionFactoryResolver);
    }

    @Before
    public void startBroker() throws Exception {
        super.startBroker();
    }

    @After
    public void stopBroker() throws Exception {
        super.stopConnectionFactoryResolver();
        super.stopBroker();
    }

    @Test
    public void handlesDownBroker() {
        Producer producer = getRetryableAmqProducerWithDownBroker();

        MessageService messageService = new MessageService(producer);
        boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

        Assert.assertFalse("hasSent", hasSent);
    }
}

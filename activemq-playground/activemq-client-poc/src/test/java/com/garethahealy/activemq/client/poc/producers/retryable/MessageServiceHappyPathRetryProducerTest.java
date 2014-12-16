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
package com.garethahealy.activemq.client.poc.producers.retryable;

import javax.jms.JMSException;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.BaseBroker;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.producers.RetryableAmqProducer;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.services.MessageService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageServiceHappyPathRetryProducerTest extends BaseBroker {

    private Producer getRetryableAmqProducer(String queueName) throws JMSException {
        RetryConfiguration retryConfiguration = new RetryConfiguration();
        BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
        connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

        super.createConsumer(queueName, brokerConfiguration);

        return new RetryableAmqProducer(retryConfiguration, brokerConfiguration, connectionFactoryResolver);
    }

    @Before
    public void startBroker() throws Exception {
        super.startBroker();
    }

    @After
    public void stopBroker() throws Exception {
        super.stopConnectionFactoryResolver();
        super.stopBroker();
        super.closeConsumer();
    }

    @Test
    public void canSend() throws JMSException {
        Producer producer = getRetryableAmqProducer("hello");

        MessageService messageService = new MessageService(producer);
        boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

        Assert.assertTrue("hasSent", hasSent);

        int count = getMessageCount();
        Assert.assertEquals(1, count);
    }
}

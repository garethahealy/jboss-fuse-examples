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
package com.garethahealy.activemq.client.poc.producers.performance;

import java.util.concurrent.TimeUnit;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.producers.BaseBroker;
import com.garethahealy.activemq.client.poc.producers.DefaultAmqProducer;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.resolvers.AmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.services.MessageService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageServiceComparisonTest extends BaseBroker {

    private static final Logger LOG = LoggerFactory.getLogger(MessageServiceComparisonTest.class);

    private Producer getNonPooledAmqProducer() {
        BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
        ConnectionFactoryResolver connectionFactoryResolver = new AmqConnectionFactoryResolver(brokerConfiguration);

        return new DefaultAmqProducer(brokerConfiguration, connectionFactoryResolver);
    }

    private Producer getPooledAmqProducer() {
        BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
        ConnectionFactoryResolver connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

        return new DefaultAmqProducer(brokerConfiguration, connectionFactoryResolver);
    }

    @Before
    public void startBroker() throws Exception {
        super.startBroker();
    }

    @After
    public void stopBroker() throws Exception {
        super.stopBroker();
    }

    @Test
    public void sendThousandMessages() {
        Producer nonPooledAmqProducerproducer = getNonPooledAmqProducer();
        Producer pooledAmqProducer = getPooledAmqProducer();

        long nonPooledStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            MessageService messageService = new MessageService(nonPooledAmqProducerproducer);
            boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

            Assert.assertTrue("hasSent", hasSent);
        }
        long nonPooledEnd = System.nanoTime();

        long pooledStart = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            MessageService messageService = new MessageService(pooledAmqProducer);
            boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

            Assert.assertTrue("hasSent", hasSent);
        }
        long pooledEnd = System.nanoTime();

        long diffNonPooled = nonPooledEnd - nonPooledStart;
        long diffPooled = pooledEnd - pooledStart;
        long diff = diffNonPooled - diffPooled;

        LOG.info("NonPooled: {}nano seconds", diffNonPooled);
        LOG.info("Pooled: {}nano seconds", diffPooled);
        LOG.info("Diff: {}seconds", TimeUnit.NANOSECONDS.toSeconds(diff));

        Assert.assertTrue("pooled is faster", diffNonPooled > diffPooled);
    }
}

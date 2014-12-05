package com.garethahealy.activemq.client.poc.producers.string;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.producers.BaseBroker;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.producers.StringAmqProducer;
import com.garethahealy.activemq.client.poc.services.MessageService;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageServiceHappyPathTest extends BaseBroker {

        private Producer getStringAmqProducer() {
                BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
                ConnectionFactoryResolver connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

                return new StringAmqProducer(brokerConfiguration, connectionFactoryResolver);
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
        public void canSend() {
                Producer producer = getStringAmqProducer();

                MessageService messageService = new MessageService(producer);
                boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

                Assert.assertTrue("hasSent", hasSent);
        }
}

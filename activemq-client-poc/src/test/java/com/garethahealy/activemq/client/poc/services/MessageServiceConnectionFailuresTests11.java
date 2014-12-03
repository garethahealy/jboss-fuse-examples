package com.garethahealy.activemq.client.poc.services;

import com.garethahealy.activemq.client.poc.callbacks.CreateConnectionCallback;
import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.mocked.producers.MockedSlowRetryableAmqProducer;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageServiceConnectionFailuresTests11 extends BaseBroker {

        private Producer getRetryableAmqProducer() {
                RetryConfiguration retryConfiguration = new RetryConfiguration();
                AmqBrokerConfiguration amqBrokerConfiguration = new AmqBrokerConfiguration();
                ConnectionFactoryResolver connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(amqBrokerConfiguration);

                CreateConnectionCallback createConnectionCallback = new CreateConnectionCallback() {
                        @Override
                        public void onConnect() {
                                try {
                                        stopBroker();
                                }catch (Exception ex) {
                                }
                        }
                };

                return new MockedSlowRetryableAmqProducer(createConnectionCallback, retryConfiguration, amqBrokerConfiguration, connectionFactoryResolver);
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
                Producer producer = getRetryableAmqProducer();

                MessageService messageService = new MessageService(producer);
                boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

                Assert.assertTrue("hasSent", hasSent);
        }
}

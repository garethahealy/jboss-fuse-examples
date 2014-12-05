package com.garethahealy.activemq.client.poc.producers.retryable;

import com.garethahealy.activemq.client.poc.callbacks.DefaultCallbackHandler;
import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.mocked.producers.CallbackableRetryableAmqProducer;
import com.garethahealy.activemq.client.poc.producers.BaseBroker;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.services.MessageService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageServiceBrokerDownAfterMessageCreatedTest extends BaseBroker {

        private Producer getRetryableAmqProducerWithDownBroker() {
                RetryConfiguration retryConfiguration = new RetryConfiguration();
                BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
                ConnectionFactoryResolver connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

                DefaultCallbackHandler defaultCallbackHandler = new DefaultCallbackHandler() {
                        @Override
                        public void createMessage() {
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

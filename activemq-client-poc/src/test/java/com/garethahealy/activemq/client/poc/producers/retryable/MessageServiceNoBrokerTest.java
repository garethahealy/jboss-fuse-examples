package com.garethahealy.activemq.client.poc.producers.retryable;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.Producer;
import com.garethahealy.activemq.client.poc.producers.RetryableAmqProducer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.resolvers.PooledAmqConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.services.MessageService;
import org.junit.Assert;
import org.junit.Test;

public class MessageServiceNoBrokerTest {

        private Producer getRetryableAmqProducer() {
                RetryConfiguration retryConfiguration = new RetryConfiguration();
                BrokerConfiguration brokerConfiguration = new BrokerConfiguration();
                ConnectionFactoryResolver connectionFactoryResolver = new PooledAmqConnectionFactoryResolver(brokerConfiguration);

                return new RetryableAmqProducer(retryConfiguration, brokerConfiguration, connectionFactoryResolver);
        }

        @Test
        public void handlesBrokerRunning() {
                Producer producer = getRetryableAmqProducer();

                MessageService messageService = new MessageService(producer);
                boolean hasSent = messageService.sendMessagesToQueue("gareth", "healy");

                Assert.assertFalse("hasSent", hasSent);
        }
}

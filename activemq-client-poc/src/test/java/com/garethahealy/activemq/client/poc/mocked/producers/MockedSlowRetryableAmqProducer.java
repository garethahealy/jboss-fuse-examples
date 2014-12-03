package com.garethahealy.activemq.client.poc.mocked.producers;

import com.garethahealy.activemq.client.poc.callbacks.CreateConnectionCallback;
import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.RetryableAmqProducer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class MockedSlowRetryableAmqProducer extends RetryableAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(MockedSlowRetryableAmqProducer.class);

        private CreateConnectionCallback createConnectionCallback;

        public MockedSlowRetryableAmqProducer(CreateConnectionCallback createConnectionCallback,
                                              RetryConfiguration retryConfiguration, AmqBrokerConfiguration amqBrokerConfiguration,
                                              ConnectionFactoryResolver connectionFactoryResolver) {

                super(retryConfiguration, amqBrokerConfiguration, connectionFactoryResolver);

                this.createConnectionCallback = createConnectionCallback;
        }

        @Override
        protected Connection createConnection() throws JMSException {
                Connection amqConnection = super.createConnection();

                createConnectionCallback.onConnect();
                return amqConnection;
        }
}

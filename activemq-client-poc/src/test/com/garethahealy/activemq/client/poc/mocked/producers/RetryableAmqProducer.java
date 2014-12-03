package com.garethahealy.activemq.client.poc.mocked.producers;

import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.DefaultAmqProducer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class RetryableAmqProducer extends DefaultAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(RetryableAmqProducer.class);
        private RetryConfiguration retryConfiguration;

        public RetryableAmqProducer(RetryConfiguration retryConfiguration, AmqBrokerConfiguration amqBrokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                super(amqBrokerConfiguration, connectionFactoryResolver);

                this.retryConfiguration = retryConfiguration;
        }

        @Override
        protected Connection createConnection() {
                Connection amqConnection = null;

                int count = 1;
                int connectionRetryAmount = retryConfiguration.getConnectionRetryAmount();
                while (amqConnection == null) {
                        try {
                                amqConnection = super.createConnection();
                        } catch (JMSException ex) {
                                LOG.error("Exception creating connection from connection factory {} to {} because {}. Attenpting retry {} of {}",
                                          connectionFactory.getClass().getName(), amqBrokerConfiguration.getBrokerURL(), ex, count, connectionRetryAmount);
                        }

                        count++;

                        if (count > connectionRetryAmount) {
                                break;
                        }
                }

                return amqConnection;
        }
}

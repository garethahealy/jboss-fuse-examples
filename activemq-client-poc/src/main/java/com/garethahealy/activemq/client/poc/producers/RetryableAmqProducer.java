package com.garethahealy.activemq.client.poc.producers;

import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class RetryableAmqProducer extends DefaultAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(RetryableAmqProducer.class);
        private RetryConfiguration retryConfiguration;

        public RetryableAmqProducer(RetryConfiguration retryConfiguration, AmqBrokerConfiguration amqBrokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                super(amqBrokerConfiguration, connectionFactoryResolver);

                this.retryConfiguration = retryConfiguration;
        }

        @Override
        protected Connection createConnection() throws JMSException {
                Connection amqConnection = null;

                int count = 1;
                int retryAmount = retryConfiguration.getConnectionRetryAmount();
                while (amqConnection == null) {
                        try {
                                amqConnection = super.createConnection();
                        } catch (JMSException ex) {
                                LOG.error("Exception creating connection from connection factory {} to {} because {}. Attempting retry {} of {}",
                                          connectionFactory.getClass().getName(), amqBrokerConfiguration.getBrokerURL(), ex.getMessage(), count, retryAmount);

                                if (count == retryAmount) {
                                        //Last retry, so bubble exception upwards
                                        throw ex;
                                }
                        }

                        count++;

                        if (count > retryAmount) {
                                break;
                        }
                }

                return amqConnection;
        }

        @Override
        protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) throws JMSException {
                Session amqSession = null;
                if (amqConnection != null) {
                        int count = 1;
                        int retryAmount = retryConfiguration.getConnectionRetryAmount();
                        while (amqSession == null) {
                                try {
                                        amqSession = super.createSession(amqConnection, isTransacted, acknowledgeMode);
                                } catch (JMSException ex) {
                                        LOG.error("Exception creating session for connection {} because {}. Attempting retry {} of {}", amqConnection, ex.getMessage(),  count, retryAmount);

                                        if (count == retryAmount) {
                                                //Last retry, so bubble exception upwards
                                                throw ex;
                                        }
                                }

                                count++;

                                if (count > retryAmount) {
                                        break;
                                }
                        }
                }

                return amqSession;
        }

        @Override
        protected Queue createQueue(Session amqSession, String queueName) throws JMSException {
                return super.createQueue(amqSession, queueName);
        }

        @Override
        protected MessageProducer createProducer(Session amqSession, Queue amqQueue) throws JMSException {
                return super.createProducer(amqSession, amqQueue);
        }

        @Override
        protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
                return super.createMessage(amqSession, body);
        }
}

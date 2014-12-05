package com.garethahealy.activemq.client.poc.producers;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class DefaultAmqProducer extends BaseAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(DefaultAmqProducer.class);

        public DefaultAmqProducer(BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                super(brokerConfiguration, connectionFactoryResolver);
        }

        @Override
        protected Connection createConnection() throws JMSException {
                Connection amqConnection = null;

                try {
                        amqConnection = super.createConnection();
                } catch (JMSException ex) {
                        LOG.error("Exception creating connection from connection factory {} to {} because {}", connectionFactory.getClass().getName(),
                                  brokerConfiguration.getBrokerURL(), ex.getMessage());

                        throw ex;
                }

                return amqConnection;
        }

        @Override
        protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) throws JMSException {
                Session amqSession = null;
                if (amqConnection != null) {
                        try {
                                amqSession = super.createSession(amqConnection, isTransacted, acknowledgeMode);
                        } catch (JMSException ex) {
                                LOG.error("Exception creating session for connection {} because {}", amqConnection, ex.getMessage());

                                throw ex;
                        }
                }

                return amqSession;
        }

        @Override
        protected Queue createQueue(Session amqSession, String queueName) throws JMSException {
                Queue amqQueue = null;
                if (amqSession != null) {
                        try {
                                amqQueue = super.createQueue(amqSession, queueName);
                        } catch (JMSException ex) {
                                LOG.error("Exception creating queue {} for session because {}", queueName, amqSession, ex.getMessage());

                                throw ex;
                        }
                }

                return amqQueue;
        }

        @Override
        protected MessageProducer createProducer(Session amqSession, Queue amqQueue) throws JMSException {
                MessageProducer amqProducer = null;
                if (amqSession != null) {
                        try {
                                amqProducer = super.createProducer(amqSession, amqQueue);
                        } catch (JMSException ex) {
                                LOG.error("Exception creating producer for session {} on queue {} because {}", amqSession, amqQueue, ex.getMessage());

                                throw ex;
                        }
                }

                return amqProducer;
        }

        @Override
        protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
                Message amqMessage = null;
                if (amqSession != null) {
                        try {
                                amqMessage = super.createMessage(amqSession, body);
                        } catch (JMSException ex) {
                                LOG.error("Exception creating message {} for session {} because {}", body, amqSession, ex.getMessage());

                                throw ex;
                        }
                }

                return amqMessage;
        }

        @Override
        protected void send(MessageProducer amqProducer, Message amqMessage) throws JMSException {
                if (amqProducer != null) {
                        try {
                                super.send(amqProducer, amqMessage);
                        } catch (JMSException ex) {
                                LOG.error("Exception sending message {} to producer {} because {}", amqMessage, amqProducer, ex.getMessage().toString());

                                throw ex;
                        }
                }
        }
}

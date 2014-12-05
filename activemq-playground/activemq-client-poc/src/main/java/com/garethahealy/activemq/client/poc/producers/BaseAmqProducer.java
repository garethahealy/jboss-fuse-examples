package com.garethahealy.activemq.client.poc.producers;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;
import com.garethahealy.activemq.client.poc.errorstrategys.DefaultErrorStrategy;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;

public abstract class BaseAmqProducer implements Producer {

        private static final Logger LOG = LoggerFactory.getLogger(BaseAmqProducer.class);

        private ConnectionFactoryResolver connectionFactoryResolver;
        private AmqErrorStrategy amqErrorStrategy;
        protected BrokerConfiguration brokerConfiguration;
        protected ConnectionFactory connectionFactory;

        public BaseAmqProducer(BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                this(brokerConfiguration, connectionFactoryResolver, new DefaultErrorStrategy());
        }

        public BaseAmqProducer(BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver, AmqErrorStrategy amqErrorStrategy) {
                this.brokerConfiguration = brokerConfiguration;
                this.connectionFactoryResolver = connectionFactoryResolver;
                this.amqErrorStrategy = amqErrorStrategy;
        }

        private void init() {
                connectionFactory = connectionFactoryResolver.start();
        }

        public boolean produce(String queueName, Object[] body) {
                if (connectionFactory == null) {
                        init();
                }

                Session amqSession = null;
                Connection amqConnection = null;
                MessageProducer amqProducer = null;

                boolean hasNotThrownException = true;
                try {
                        //Get a connection and session from the factory
                        amqConnection = createConnection();
                        amqSession = createSession(amqConnection, brokerConfiguration.isTransacted(), brokerConfiguration.getAcknowledgeMode());

                        //Create the queue and wrap the body as an object so we can send it
                        Queue amqQueue = createQueue(amqSession, queueName);
                        amqProducer = createProducer(amqSession, amqQueue);
                        Message amqMessage = createMessage(amqSession, body);

                        //Send the object
                        send(amqProducer, amqMessage);
                } catch (JMSException ex) {
                        hasNotThrownException = false;

                        LOG.error("Exception producing message {} because {}", body, ExceptionUtils.getStackTrace(ex));

                        try {
                                amqErrorStrategy.handle(ex, queueName, body);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                //Cleanup
                safeCloseSession(amqSession);
                safeCloseConnection(amqConnection);
                safeCloseMessageProducer(amqProducer);

                return hasNotThrownException;
        }

        protected Connection createConnection() throws JMSException {
                return connectionFactory.createConnection(brokerConfiguration.getUsername(), brokerConfiguration.getPassword());
        }

        protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) throws JMSException {
                return amqConnection.createSession(isTransacted, acknowledgeMode);
        }

        protected Queue createQueue(Session amqSession, String queueName) throws JMSException {
                return amqSession.createQueue(queueName);
        }

        protected MessageProducer createProducer(Session amqSession, Queue amqQueue) throws JMSException {
                return amqSession.createProducer(amqQueue);
        }

        protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
                return amqSession.createObjectMessage(body);
        }

        protected void send(MessageProducer amqProducer, Message amqMessage) throws JMSException {
                amqProducer.send(amqMessage);
        }

        private boolean safeCloseSession(Session amqSession) {
                if (amqSession != null) {
                        try {
                                amqSession.close();
                                amqSession = null;
                        } catch (JMSException ex) {
                                LOG.error("Exception closing session {} because {}", amqSession, ExceptionUtils.getStackTrace(ex));
                        }

                }

                return amqSession == null;
        }

        private boolean safeCloseConnection(Connection amqConnection) {
                if (amqConnection != null) {
                        try {
                                amqConnection.close();
                                amqConnection = null;
                        } catch (JMSException ex) {
                                LOG.error("Exception closing connection {} because {}", amqConnection, ExceptionUtils.getStackTrace(ex));
                        }

                }

                return amqConnection == null;
        }

        private boolean safeCloseMessageProducer(MessageProducer amqProducer) {
                if (amqProducer != null) {
                        try {
                                amqProducer.close();
                                amqProducer = null;
                        } catch (JMSException ex) {
                                LOG.error("Exception closing producer {} because {}", amqProducer, ExceptionUtils.getStackTrace(ex));
                        }
                }

                return amqProducer == null;
        }
}

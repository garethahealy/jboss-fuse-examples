package com.garethahealy.activemq.client.poc.mocked.producers;

import com.garethahealy.activemq.client.poc.callbacks.DefaultCallbackHandler;
import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.producers.RetryableAmqProducer;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class CallbackableRetryableAmqProducer extends RetryableAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(CallbackableRetryableAmqProducer.class);

        private DefaultCallbackHandler defaultCallbackHandler;

        public CallbackableRetryableAmqProducer(DefaultCallbackHandler defaultCallbackHandler,
                                                RetryConfiguration retryConfiguration, AmqBrokerConfiguration amqBrokerConfiguration,
                                                ConnectionFactoryResolver connectionFactoryResolver) {

                super(retryConfiguration, amqBrokerConfiguration, connectionFactoryResolver);

                this.defaultCallbackHandler = defaultCallbackHandler;
        }

        @Override
        protected Connection createConnection() throws JMSException {
                Connection amqConnection = super.createConnection();

                defaultCallbackHandler.createConnection();
                return amqConnection;
        }

        @Override
        protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) throws JMSException {
                Session session = super.createSession(amqConnection, isTransacted, acknowledgeMode);

                defaultCallbackHandler.createSession();
                return session;
        }
}

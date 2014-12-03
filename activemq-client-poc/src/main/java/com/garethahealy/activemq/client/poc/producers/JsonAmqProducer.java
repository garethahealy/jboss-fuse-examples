package com.garethahealy.activemq.client.poc.producers;

import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class JsonAmqProducer extends DefaultAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(JsonAmqProducer.class);

        public JsonAmqProducer(AmqBrokerConfiguration amqBrokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                super(amqBrokerConfiguration, connectionFactoryResolver);
        }

        @Override
        protected Message createMessage(Session amqSession, Object[] body) {
                Message amqMessage = null;
                try {
                        amqMessage = amqSession.createTextMessage(body[0].toString() + body[1].toString());
                } catch (JMSException ex) {
                        LOG.error("Exception creating message {} for session {} because {}", body, amqSession, ex);
                }

                return amqMessage;
        }

}

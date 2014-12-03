package com.garethahealy.activemq.client.poc.producers;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class StringAmqProducer extends DefaultAmqProducer {

        private static final Logger LOG = LoggerFactory.getLogger(StringAmqProducer.class);

        public StringAmqProducer(BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
                super(brokerConfiguration, connectionFactoryResolver);
        }

        @Override
        protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
                Message amqMessage = null;
                try {
                        amqMessage = amqSession.createTextMessage(body[0].toString() + body[1].toString());
                } catch (JMSException ex) {
                        LOG.error("Exception creating message {} for session {} because {}", body, amqSession, ExceptionUtils.getStackTrace(ex));

                        throw ex;
                }

                return amqMessage;
        }

}

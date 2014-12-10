/*
 * #%L
 * activemq-client-poc
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.activemq.client.poc.producers;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAmqProducer extends BaseAmqProducer {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAmqProducer.class);

    public DefaultAmqProducer(BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
        super(brokerConfiguration, connectionFactoryResolver);
    }

    @Override
    protected Connection createConnection() {
        Connection amqConnection = null;

        try {
            amqConnection = super.createConnection();
        } catch (JMSException ex) {
            LOG.error("Exception creating connection from connection factory {} to {} because {}", connectionFactory.getClass().getName(),
                      brokerConfiguration.getBrokerURL(), ex.getMessage());
        }

        return amqConnection;
    }

    @Override
    protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) {
        Session amqSession = null;
        if (amqConnection != null) {
            try {
                amqSession = super.createSession(amqConnection, isTransacted, acknowledgeMode);
            } catch (JMSException ex) {
                LOG.error("Exception creating session for connection {} because {}", amqConnection, ex.getMessage());
            }
        }

        return amqSession;
    }

    @Override
    protected Queue createQueue(Session amqSession, String queueName) {
        Queue amqQueue = null;
        if (amqSession != null) {
            try {
                amqQueue = super.createQueue(amqSession, queueName);
            } catch (JMSException ex) {
                LOG.error("Exception creating queue {} for session because {}", queueName, amqSession, ex.getMessage());
            }
        }

        return amqQueue;
    }

    @Override
    protected MessageProducer createProducer(Session amqSession, Queue amqQueue) {
        MessageProducer amqProducer = null;
        if (amqSession != null) {
            try {
                amqProducer = super.createProducer(amqSession, amqQueue);
            } catch (JMSException ex) {
                LOG.error("Exception creating producer for session {} on queue {} because {}", amqSession, amqQueue, ex.getMessage());
            }
        }

        return amqProducer;
    }

    @Override
    protected Message createMessage(Session amqSession, Object[] body) {
        Message amqMessage = null;
        if (amqSession != null) {
            try {
                amqMessage = super.createMessage(amqSession, body);
            } catch (JMSException ex) {
                LOG.error("Exception creating message {} for session {} because {}", body, amqSession, ex.getMessage());
            }
        }

        return amqMessage;
    }

    @Override
    protected void send(MessageProducer amqProducer, Message amqMessage) {
        if (amqProducer != null) {
            try {
                super.send(amqProducer, amqMessage);
            } catch (JMSException ex) {
                LOG.error("Exception sending message {} to producer {} because {}", amqMessage, amqProducer, ex.getMessage());
            }
        }
    }
}

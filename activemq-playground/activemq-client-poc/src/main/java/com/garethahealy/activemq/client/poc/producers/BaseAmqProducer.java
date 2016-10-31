/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: ActiveMQ Playground :: Client POC
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;
import com.garethahealy.activemq.client.poc.errorstrategys.DefaultErrorStrategy;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseAmqProducer implements Producer {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAmqProducer.class);

    protected BrokerConfiguration brokerConfiguration;
    protected ConnectionFactory connectionFactory;
    private ConnectionFactoryResolver connectionFactoryResolver;
    private AmqErrorStrategy amqErrorStrategy;

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

            amqErrorStrategy.handle(ex, queueName, body);
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

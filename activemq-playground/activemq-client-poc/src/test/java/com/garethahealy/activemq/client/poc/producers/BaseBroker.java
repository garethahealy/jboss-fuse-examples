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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBroker {

    private static final Logger LOG = LoggerFactory.getLogger(BaseBroker.class);

    protected BrokerService broker;
    protected ConnectionFactoryResolver connectionFactoryResolver;

    private Session session;
    private Connection connection;
    private MessageConsumer messageConsumer;

    protected void startBroker() throws Exception {
        broker = new BrokerService();
        broker.setPersistent(false);

        TransportConnector connector = broker.addConnector(new TransportConnector());
        connector.setUri(new URI("tcp://0.0.0.0:61616"));
        connector.setName("tcp");

        broker.start();
        broker.waitUntilStarted();
        broker.deleteAllMessages();
    }

    protected void createConsumer(String queueName, BrokerConfiguration brokerConfiguration) throws JMSException {
        closeConsumer();

        connection = connectionFactoryResolver.start().createConnection(brokerConfiguration.getUsername(), brokerConfiguration.getPassword());
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(queueName);
        messageConsumer = session.createConsumer(queue);
    }

    protected int getMessageCount() throws JMSException {
        LOG.error("--------------------- about to read from queue");

        List<Message> messages = new ArrayList<Message>();
        Message message = new ActiveMQMessage();
        while (message != null) {
            LOG.error("--------------------- in loop...");

            message = messageConsumer.receive(1000);
            if (message == null) {
                break;
            }

            messages.add(message);
            LOG.error("--------------------- found {}", message.toString());
        }

        return messages.size();
    }

    protected void stopBroker() throws Exception {
        if (broker != null) {
            broker.stop();
            broker.waitUntilStopped();
            broker = null;
        }
    }

    protected void stopConnectionFactoryResolver() {
        if (connectionFactoryResolver != null) {
            connectionFactoryResolver.stop();
            connectionFactoryResolver = null;
        }
    }

    protected void stopAnyConnectionFactoryResolver(ConnectionFactoryResolver connectionFactoryResolver) {
        if (connectionFactoryResolver != null) {
            connectionFactoryResolver.stop();
            connectionFactoryResolver = null;
        }
    }

    protected void closeConsumer() throws JMSException {
        if (session != null) {
            session.close();
            session = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }

        if (messageConsumer != null) {
            messageConsumer.close();
            messageConsumer = null;
        }
    }
}

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

import java.io.Serializable;
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
import org.apache.activemq.command.ActiveMQObjectMessage;

public abstract class BaseBroker {

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
        connection.start();

        session = connection.createSession(brokerConfiguration.isTransacted(), brokerConfiguration.getAcknowledgeMode());
        Queue queue = session.createQueue(queueName);
        messageConsumer = session.createConsumer(queue);
    }

    protected List<Object[]> getMessagesFromBroker() throws JMSException {
        List<Object[]> messages = new ArrayList<Object[]>();
        Message message = new ActiveMQMessage();
        while (message != null) {
            message = messageConsumer.receive(1000);
            if (message == null) {
                break;
            }

            Serializable obj = ((ActiveMQObjectMessage)message).getObject();
            if (obj != null && obj instanceof Object[]) {
                messages.add((Object[])obj);
            }
        }

        return messages;
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

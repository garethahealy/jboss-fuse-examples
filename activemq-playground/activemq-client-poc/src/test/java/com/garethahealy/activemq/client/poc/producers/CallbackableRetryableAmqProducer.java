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

import com.garethahealy.activemq.client.poc.callbacks.DefaultCallbackHandler;
import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class CallbackableRetryableAmqProducer extends RetryableAmqProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CallbackableRetryableAmqProducer.class);

    private DefaultCallbackHandler defaultCallbackHandler;

    public CallbackableRetryableAmqProducer(DefaultCallbackHandler defaultCallbackHandler, RetryConfiguration retryConfiguration, BrokerConfiguration brokerConfiguration,
                                            ConnectionFactoryResolver connectionFactoryResolver) {
        super(retryConfiguration, brokerConfiguration, connectionFactoryResolver);

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

    @Override
    protected Queue createQueue(Session amqSession, String queueName) throws JMSException {
        Queue queue = super.createQueue(amqSession, queueName);

        defaultCallbackHandler.createQueue();
        return queue;
    }

    @Override
    protected MessageProducer createProducer(Session amqSession, Queue amqQueue) throws JMSException {
        MessageProducer messageProducer = super.createProducer(amqSession, amqQueue);

        defaultCallbackHandler.createProducer();
        return messageProducer;
    }

    @Override
    protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
        Message message = super.createMessage(amqSession, body);

        defaultCallbackHandler.createMessage();
        return message;
    }
}

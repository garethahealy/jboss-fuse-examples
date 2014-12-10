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

import com.garethahealy.activemq.client.poc.callbacks.RetryLoopCallback;
import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryableAmqProducer extends BaseAmqProducer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryableAmqProducer.class);
    private RetryConfiguration retryConfiguration;

    public RetryableAmqProducer(RetryConfiguration retryConfiguration, BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
        super(brokerConfiguration, connectionFactoryResolver);

        this.retryConfiguration = retryConfiguration;
    }

    @Override
    protected Connection createConnection() throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public Connection runAndGetResult() throws JMSException {
                return RetryableAmqProducer.super.createConnection();
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception creating connection from connection factory {} to {} because {}. Attempting retry {} of {}",
                          getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        return retryWhileResultIsNull(retryConfiguration.getCreateConnectionRetryCount(), callback, connectionFactory.getClass().getName(), brokerConfiguration.getBrokerURL());
    }

    @Override
    protected Session createSession(final Connection amqConnection, final boolean isTransacted, final int acknowledgeMode) throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public Session runAndGetResult() throws JMSException {
                return RetryableAmqProducer.super.createSession(amqConnection, isTransacted, acknowledgeMode);
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception creating session for connection {} because {}. Attempting retry {} of {}", getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        return retryWhileResultIsNull(retryConfiguration.getCreateSessionRetryCount(), callback, amqConnection);
    }

    @Override
    protected Queue createQueue(final Session amqSession, final String queueName) throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public Queue runAndGetResult() throws JMSException {
                return RetryableAmqProducer.super.createQueue(amqSession, queueName);
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception creating queue {} for session because {}. Attempting retry {} of {}", getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        return retryWhileResultIsNull(retryConfiguration.getCreateQueueRetryCount(), callback, queueName, amqSession);
    }

    @Override
    protected MessageProducer createProducer(final Session amqSession, final Queue amqQueue) throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public MessageProducer runAndGetResult() throws JMSException {
                return RetryableAmqProducer.super.createProducer(amqSession, amqQueue);
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception creating producer for session {} on queue {} because {}. Attempting retry {} of {}", getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        return retryWhileResultIsNull(retryConfiguration.getCreateProducerRetryCount(), callback, amqSession, amqQueue);
    }

    @Override
    protected Message createMessage(final Session amqSession, final Object[] body) throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public Message runAndGetResult() throws JMSException {
                return RetryableAmqProducer.super.createMessage(amqSession, body);
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception creating message {} for session {} because {}. Attempting retry {} of {}", getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        return retryWhileResultIsNull(retryConfiguration.getCreateMessageRetryCount(), callback, body, amqSession);
    }

    @Override
    protected void send(final MessageProducer amqProducer, final Message amqMessage) throws JMSException {
        RetryLoopCallback callback = new RetryLoopCallback() {
            @Override
            public void run() throws JMSException {
                RetryableAmqProducer.super.send(amqProducer, amqMessage);
            }

            @Override
            public void log(JMSException ex, int count, int retryAmount, Object... arguments) {
                LOG.error("Exception sending message {} to producer {} because {}. Attempting retry {} of {}", getLoggingArguments(ex, count, retryAmount, arguments));
            }
        };

        retryWhileCountLessThan(retryConfiguration.getSendRetryCount(), callback, amqMessage, amqProducer);
    }

    private <T> T retryWhileResultIsNull(int retryCount, RetryLoopCallback callback, Object... logging) throws JMSException {
        T result = null;
        int count = 1;
        while (result == null) {
            try {
                result = callback.runAndGetResult();
            } catch (JMSException ex) {
                callback.log(ex, count, retryCount, logging);

                if (count >= retryCount) {
                    //Last retry, so bubble exception upwards
                    throw ex;
                }
            }

            count++;

            if (count > retryCount) {
                break;
            }
        }

        return result;
    }

    private void retryWhileCountLessThan(int retryCount, RetryLoopCallback callback, Object... logging) throws JMSException {
        int count = 1;
        while (count <= retryCount) {
            try {
                callback.run();
            } catch (JMSException ex) {
                callback.log(ex, count, retryCount, logging);

                if (count >= retryCount) {
                    //Last retry, so bubble exception upwards
                    throw ex;
                }
            }

            count++;

            if (count > retryCount) {
                break;
            }
        }
    }
}

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

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import com.garethahealy.activemq.client.poc.config.RetryConfiguration;
import com.garethahealy.activemq.client.poc.resolvers.ConnectionFactoryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class RetryableAmqProducer extends BaseAmqProducer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryableAmqProducer.class);
    private RetryConfiguration retryConfiguration;

    public RetryableAmqProducer(RetryConfiguration retryConfiguration, BrokerConfiguration brokerConfiguration, ConnectionFactoryResolver connectionFactoryResolver) {
        super(brokerConfiguration, connectionFactoryResolver);

        this.retryConfiguration = retryConfiguration;
    }

    @Override
    protected Connection createConnection() throws JMSException {
        Connection amqConnection = null;

        int count = 1;
        int retryAmount = retryConfiguration.getCreateConnectionRetryCount();
        while (amqConnection == null) {
            try {
                amqConnection = super.createConnection();
            } catch (JMSException ex) {
                LOG.error("Exception creating connection from connection factory {} to {} because {}. Attempting retry {} of {}",
                          connectionFactory.getClass().getName(), brokerConfiguration.getBrokerURL(), ex.getMessage(), count, retryAmount);

                if (count >= retryAmount) {
                    //Last retry, so bubble exception upwards
                    throw ex;
                }
            }

            count++;

            if (count > retryAmount) {
                break;
            }
        }

        return amqConnection;
    }

    @Override
    protected Session createSession(Connection amqConnection, boolean isTransacted, int acknowledgeMode) throws JMSException {
        Session amqSession = null;
        if (amqConnection != null) {
            int count = 1;
            int retryAmount = retryConfiguration.getCreateSessionRetryCount();
            while (amqSession == null) {
                try {
                    amqSession = super.createSession(amqConnection, isTransacted, acknowledgeMode);
                } catch (JMSException ex) {
                    LOG.error("Exception creating session for connection {} because {}. Attempting retry {} of {}", amqConnection, ex.getMessage(), count,
                              retryAmount);

                    if (count >= retryAmount) {
                        //Last retry, so bubble exception upwards
                        throw ex;
                    }
                }

                count++;

                if (count > retryAmount) {
                    break;
                }
            }
        }

        return amqSession;
    }

    @Override
    protected Queue createQueue(Session amqSession, String queueName) throws JMSException {
        Queue amqQueue = null;
        if (amqSession != null) {
            int count = 1;
            int retryAmount = retryConfiguration.getCreateQueueRetryCount();
            while (amqQueue == null) {
                try {
                    amqQueue = super.createQueue(amqSession, queueName);
                } catch (JMSException ex) {
                    LOG.error("Exception creating queue {} for session because {}. Attempting retry {} of {}", queueName, amqSession, ex.getMessage(), count,
                              retryAmount);

                    if (count >= retryAmount) {
                        //Last retry, so bubble exception upwards
                        throw ex;
                    }
                }

                count++;

                if (count > retryAmount) {
                    break;
                }
            }
        }

        return amqQueue;
    }

    @Override
    protected MessageProducer createProducer(Session amqSession, Queue amqQueue) throws JMSException {
        MessageProducer amqProducer = null;
        if (amqSession != null) {
            int count = 1;
            int retryAmount = retryConfiguration.getCreateProducerRetryCount();
            while (amqProducer == null) {
                try {
                    amqProducer = super.createProducer(amqSession, amqQueue);
                } catch (JMSException ex) {
                    LOG.error("Exception creating producer for session {} on queue {} because {}. Attempting retry {} of {}", amqSession, amqQueue,
                              ex.getMessage(), count, retryAmount);

                    if (count >= retryAmount) {
                        //Last retry, so bubble exception upwards
                        throw ex;
                    }
                }

                count++;

                if (count > retryAmount) {
                    break;
                }
            }
        }

        return amqProducer;
    }

    @Override
    protected Message createMessage(Session amqSession, Object[] body) throws JMSException {
        Message amqMessage = null;
        if (amqSession != null) {
            int count = 1;
            int retryAmount = retryConfiguration.getCreateMessageRetryCount();
            while (amqMessage == null) {
                try {
                    amqMessage = super.createMessage(amqSession, body);
                } catch (JMSException ex) {
                    LOG.error("Exception creating message {} for session {} because {}. Attempting retry {} of {}", body, amqSession, ex.getMessage(), count,
                              retryAmount);

                    if (count >= retryAmount) {
                        //Last retry, so bubble exception upwards
                        throw ex;
                    }
                }

                count++;

                if (count > retryAmount) {
                    break;
                }
            }
        }

        return amqMessage;
    }

    @Override
    protected void send(MessageProducer amqProducer, Message amqMessage) throws JMSException {
        if (amqProducer != null) {
            int count = 1;
            int retryAmount = retryConfiguration.getSendRetryCount();
            while (count <= retryAmount) {
                try {
                    super.send(amqProducer, amqMessage);
                } catch (JMSException ex) {
                    LOG.error("Exception sending message {} to producer {} because {}. Attempting retry {} of {}", amqMessage, amqProducer,
                              ex.getMessage().toString(), count, retryAmount);

                    if (count >= retryAmount) {
                        //Last retry, so bubble exception upwards
                        throw ex;
                    }
                }

                count++;

                if (count > retryAmount) {
                    break;
                }
            }
        }
    }

    //todo: maybe make a generic retry block of code?
}

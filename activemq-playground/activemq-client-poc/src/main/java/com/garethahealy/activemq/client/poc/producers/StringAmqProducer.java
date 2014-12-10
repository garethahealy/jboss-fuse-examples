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
    protected Message createMessage(Session amqSession, Object[] body) {
        Message amqMessage = null;
        if (amqSession != null) {
            try {
                amqMessage = amqSession.createTextMessage(body[0].toString() + body[1].toString());
            } catch (JMSException ex) {
                LOG.error("Exception creating message {} for session {} because {}", body, amqSession, ExceptionUtils.getStackTrace(ex));
            }
        }

        return amqMessage;
    }

}

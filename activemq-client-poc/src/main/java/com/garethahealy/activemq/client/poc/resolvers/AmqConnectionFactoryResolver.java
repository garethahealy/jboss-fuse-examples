package com.garethahealy.activemq.client.poc.resolvers;

import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;

public class AmqConnectionFactoryResolver implements ConnectionFactoryResolver {

        private AmqBrokerConfiguration amqBrokerConfiguration;
        private ActiveMQConnectionFactory activeMQConnectionFactory;

        public AmqConnectionFactoryResolver(AmqBrokerConfiguration amqBrokerConfiguration) {
                this.amqBrokerConfiguration = amqBrokerConfiguration;
        }

        private void init() {
                activeMQConnectionFactory = new ActiveMQConnectionFactory();
                activeMQConnectionFactory.setBrokerURL(amqBrokerConfiguration.getBrokerURL());
                activeMQConnectionFactory.setUserName(amqBrokerConfiguration.getUsername());
                activeMQConnectionFactory.setPassword(amqBrokerConfiguration.getPassword());
        }

        public ConnectionFactory start() {
                if (activeMQConnectionFactory == null) {
                        init();
                }

                return activeMQConnectionFactory;
        }

        public void stop() {
                activeMQConnectionFactory = null;
        }
}

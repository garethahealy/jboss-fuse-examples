package com.garethahealy.activemq.client.poc.resolvers;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;

public class AmqConnectionFactoryResolver implements ConnectionFactoryResolver {

        private BrokerConfiguration brokerConfiguration;
        private ActiveMQConnectionFactory activeMQConnectionFactory;

        public AmqConnectionFactoryResolver(BrokerConfiguration brokerConfiguration) {
                this.brokerConfiguration = brokerConfiguration;
        }

        private void init() {
                activeMQConnectionFactory = new ActiveMQConnectionFactory();
                activeMQConnectionFactory.setBrokerURL(brokerConfiguration.getBrokerURL());
                activeMQConnectionFactory.setUserName(brokerConfiguration.getUsername());
                activeMQConnectionFactory.setPassword(brokerConfiguration.getPassword());
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

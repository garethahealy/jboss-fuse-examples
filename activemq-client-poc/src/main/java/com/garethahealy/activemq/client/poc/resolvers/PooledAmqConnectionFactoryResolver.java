package com.garethahealy.activemq.client.poc.resolvers;

import com.garethahealy.activemq.client.poc.config.AmqBrokerConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.ConnectionFactory;

public class PooledAmqConnectionFactoryResolver implements ConnectionFactoryResolver {

        private AmqBrokerConfiguration amqBrokerConfiguration;
        private PooledConnectionFactory pooledConnectionFactory;

        public PooledAmqConnectionFactoryResolver(AmqBrokerConfiguration amqBrokerConfiguration) {
                this.amqBrokerConfiguration = amqBrokerConfiguration;
        }

        private void init() {
                ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
                activeMQConnectionFactory.setBrokerURL(amqBrokerConfiguration.getBrokerURL());
                activeMQConnectionFactory.setUserName(amqBrokerConfiguration.getUsername());
                activeMQConnectionFactory.setPassword(amqBrokerConfiguration.getPassword());

                pooledConnectionFactory = new PooledConnectionFactory();
                pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
                pooledConnectionFactory.setMaxConnections(amqBrokerConfiguration.getMaxConnections());
                pooledConnectionFactory.setMaximumActiveSessionPerConnection(amqBrokerConfiguration.getMaximumActiveSessionPerConnection());
        }

        public ConnectionFactory start() {
                if (pooledConnectionFactory == null) {
                        init();
                }

                pooledConnectionFactory.initConnectionsPool();
                return pooledConnectionFactory;
        }

        public void stop() {
                if (pooledConnectionFactory != null) {
                        pooledConnectionFactory.stop();
                        pooledConnectionFactory = null;
                }
        }
}

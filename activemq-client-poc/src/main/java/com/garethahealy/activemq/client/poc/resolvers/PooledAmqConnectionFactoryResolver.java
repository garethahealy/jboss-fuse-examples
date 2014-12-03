package com.garethahealy.activemq.client.poc.resolvers;

import com.garethahealy.activemq.client.poc.config.BrokerConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.ConnectionFactory;

public class PooledAmqConnectionFactoryResolver implements ConnectionFactoryResolver {

        private BrokerConfiguration brokerConfiguration;
        private PooledConnectionFactory pooledConnectionFactory;

        public PooledAmqConnectionFactoryResolver(BrokerConfiguration brokerConfiguration) {
                this.brokerConfiguration = brokerConfiguration;
        }

        private void init() {
                ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
                activeMQConnectionFactory.setBrokerURL(brokerConfiguration.getBrokerURL());
                activeMQConnectionFactory.setUserName(brokerConfiguration.getUsername());
                activeMQConnectionFactory.setPassword(brokerConfiguration.getPassword());

                pooledConnectionFactory = new PooledConnectionFactory();
                pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
                pooledConnectionFactory.setMaxConnections(brokerConfiguration.getMaxConnections());
                pooledConnectionFactory.setMaximumActiveSessionPerConnection(brokerConfiguration.getMaximumActiveSessionPerConnection());
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

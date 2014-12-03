package com.garethahealy.activemq.client.poc.config;

import javax.jms.Session;

public class BrokerConfiguration {

        private String brokerURL;
        private String username;
        private String password;
        private int maxConnections;
        private int maximumActiveSessionPerConnection;
        private boolean isTransacted;
        private int acknowledgeMode;

        public BrokerConfiguration() {
                this("tcp://0.0.0.0:61616", "admin", "admin", 8, 100, false, Session.AUTO_ACKNOWLEDGE);
        }

        public BrokerConfiguration(String brokerURL, String username, String password, int maxConnections, int maximumActiveSessionPerConnection, boolean isTransacted,
                                   int acknowledgeMode) {
                this.brokerURL = brokerURL;
                this.username = username;
                this.password = password;
                this.maxConnections = maxConnections;
                this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
                this.isTransacted = isTransacted;
                this.acknowledgeMode = acknowledgeMode;
        }

        public String getBrokerURL() {
                return brokerURL;
        }

        public String getUsername() {
                return username;
        }

        public String getPassword() {
                return password;
        }

        public int getMaxConnections() {
                return maxConnections;
        }

        public int getMaximumActiveSessionPerConnection() {
                return maximumActiveSessionPerConnection;
        }

        public boolean isTransacted() {
                return isTransacted;
        }

        public int getAcknowledgeMode() {
                return acknowledgeMode;
        }

        @Override
        public String toString() {
                return "com.garethahealy.activemq.client.poc.config.BrokerConfiguration{" +
                       "brokerURL='" + brokerURL + '\'' +
                       ", username='" + username + '\'' +
                       ", password='" + password + '\'' +
                       ", maxConnections=" + maxConnections +
                       ", maximumActiveSessionPerConnection=" + maximumActiveSessionPerConnection +
                       ", isTransacted=" + isTransacted +
                       ", acknowledgeMode=" + acknowledgeMode +
                       ", transacted=" + isTransacted() +
                       '}';
        }
}

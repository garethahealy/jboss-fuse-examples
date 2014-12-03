package com.garethahealy.activemq.client.poc.config;

public class AmqBrokerConfiguration {

        private String brokerURL = "tcp://0.0.0.0:61616";
        private String username = "admin";
        private String password = "admin";
        private int maxConnections = 4;
        private int maximumActiveSessionPerConnection = 25;

        public String getBrokerURL() {
                return brokerURL;
        }

        public void setBrokerURL(String brokerURL) {
                this.brokerURL = brokerURL;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public int getMaxConnections() {
                return maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
                this.maxConnections = maxConnections;
        }

        public int getMaximumActiveSessionPerConnection() {
                return maximumActiveSessionPerConnection;
        }

        public void setMaximumActiveSessionPerConnection(int maximumActiveSessionPerConnection) {
                this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
        }
}

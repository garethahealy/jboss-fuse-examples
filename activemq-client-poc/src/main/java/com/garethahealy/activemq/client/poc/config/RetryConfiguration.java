package com.garethahealy.activemq.client.poc.config;

public class RetryConfiguration {

        private int connectionRetryAmount = 5;

        public int getConnectionRetryAmount() {
                return connectionRetryAmount;
        }

        public void setConnectionRetryAmount(int connectionRetryAmount) {
                this.connectionRetryAmount = connectionRetryAmount;
        }
}

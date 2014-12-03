package com.garethahealy.activemq.client.poc.config;

public class RetryConfiguration {

        private int createConnectionRetryCount;
        private int createSessionRetryCount;
        private int createQueueRetryCount;
        private int createProducerRetryCount;
        private int createMessageRetryCount;
        private int sendRetryCount;

        public RetryConfiguration() {
                this(5, 5, 5, 5, 5, 5);
        }

        public RetryConfiguration(int createConnectionRetryCount, int createSessionRetryCount, int createQueueRetryCount, int createProducerRetryCount, int createMessageRetryCount,
                                  int sendRetryCount) {
                this.createConnectionRetryCount = createConnectionRetryCount;
                this.createSessionRetryCount = createSessionRetryCount;
                this.createQueueRetryCount = createQueueRetryCount;
                this.createProducerRetryCount = createProducerRetryCount;
                this.createMessageRetryCount = createMessageRetryCount;
                this.sendRetryCount = sendRetryCount;
        }

        public int getCreateConnectionRetryCount() {
                return createConnectionRetryCount;
        }

        public int getCreateSessionRetryCount() {
                return createSessionRetryCount;
        }

        public int getCreateQueueRetryCount() {
                return createQueueRetryCount;
        }

        public int getCreateProducerRetryCount() {
                return createProducerRetryCount;
        }

        public int getCreateMessageRetryCount() {
                return createMessageRetryCount;
        }

        public int getSendRetryCount() {
                return sendRetryCount;
        }

        @Override
        public String toString() {
                return "com.garethahealy.activemq.client.poc.config.RetryConfiguration{" +
                       "createConnectionRetryCount=" + createConnectionRetryCount +
                       ", createSessionRetryCount=" + createSessionRetryCount +
                       ", createQueueRetryCount=" + createQueueRetryCount +
                       ", createProducerRetryCount=" + createProducerRetryCount +
                       ", createMessageRetryCount=" + createMessageRetryCount +
                       ", sendRetryCount=" + sendRetryCount +
                       '}';
        }
}

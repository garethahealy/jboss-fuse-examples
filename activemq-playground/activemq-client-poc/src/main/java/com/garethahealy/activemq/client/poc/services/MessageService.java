package com.garethahealy.activemq.client.poc.services;

import com.garethahealy.activemq.client.poc.producers.Producer;

public class MessageService {

        private Producer producer;

        public MessageService(Producer producer) {
                this.producer = producer;
        }

        public boolean sendMessagesToQueue(String firstName, String surname) {
                return producer.produce("hello", new Object[] {firstName, surname});
        }
}

package com.garethahealy.activemq.client.poc.producers;

public interface Producer {

        boolean produce(String queueName, Object[] body);
}

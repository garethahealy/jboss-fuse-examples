package com.garethahealy.activemq.client.poc.errorstrategys;

public class DefaultErrorStrategy implements AmqErrorStrategy {

        @Override
        public void handle(Throwable ex, String queueName, Object[] body) {
                //NOOP
        }
}

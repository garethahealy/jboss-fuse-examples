package com.garethahealy.activemq.client.poc.errorstrategys;

import java.io.IOException;

public interface AmqErrorStrategy {

        void handle(Throwable ex, String queueName, Object[] body) throws IOException;
}

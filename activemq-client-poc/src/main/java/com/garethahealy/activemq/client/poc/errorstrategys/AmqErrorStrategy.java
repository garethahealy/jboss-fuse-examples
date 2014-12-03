package com.garethahealy.activemq.client.poc.errorstrategys;

public interface AmqErrorStrategy {

        void handle(Throwable ex, Object[] body);
}

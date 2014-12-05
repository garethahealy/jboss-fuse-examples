package com.garethahealy.activemq.client.poc.producers.errorstrategys;

import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;

import java.io.IOException;

public class HandlerRunnable implements Runnable {

        private AmqErrorStrategy strategy;
        private String queueName;
        private String firstname;
        private String surname;

        public HandlerRunnable(AmqErrorStrategy strategy, String queueName, String firstname, String surname) {
                this.strategy = strategy;
                this.queueName = queueName;
                this.firstname = firstname;
                this.surname = surname;
        }

        public void run() {
                try {
                        for (int i = 0; i < 1000; i++) {
                                strategy.handle(null, queueName, new Object[] {firstname, surname + i});
                        }
                } catch (IOException ex) {

                }
        }
}

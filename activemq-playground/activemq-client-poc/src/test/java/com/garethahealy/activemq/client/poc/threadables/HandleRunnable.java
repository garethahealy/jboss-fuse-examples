/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: ActiveMQ Playground :: Client POC
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.activemq.client.poc.threadables;

import javax.jms.JMSException;

import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;

public class HandleRunnable<T> implements Runnable {

    private AmqErrorStrategy<T> strategy;
    private String queueName;
    private String firstname;
    private String surname;

    public HandleRunnable(AmqErrorStrategy<T> strategy, String queueName, String firstname, String surname) {
        this.strategy = strategy;
        this.queueName = queueName;
        this.firstname = firstname;
        this.surname = surname;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            strategy.handle(new JMSException("Because"), queueName, new Object[] {firstname, surname + i});
        }
    }
}

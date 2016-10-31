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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;

public class GetBackedupLinesCallable<T> implements Callable<T> {

    private AmqErrorStrategy strategy;
    private String queueName;
    private int i;

    public GetBackedupLinesCallable(AmqErrorStrategy strategy, String queueName, int i) {
        this.strategy = strategy;
        this.queueName = queueName;
        this.i = i;
    }

    public T call() {
        try {
            TimeUnit.MILLISECONDS.sleep(100 * i);
        } catch (InterruptedException ex) {
            //ignore
        }

        return (T)strategy.getBackedupLines(queueName);
    }
}

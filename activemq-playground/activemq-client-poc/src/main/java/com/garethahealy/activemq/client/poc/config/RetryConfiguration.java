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
package com.garethahealy.activemq.client.poc.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RetryConfiguration {

    private int createConnectionRetryCount;
    private int createSessionRetryCount;
    private int createQueueRetryCount;
    private int createProducerRetryCount;
    private int createMessageRetryCount;
    private int sendRetryCount;
    private long sleepBetweenRetryInSeconds;

    public RetryConfiguration() {
        this(5, 5, 5, 5, 5, 5, 1);
    }

    public RetryConfiguration(int createConnectionRetryCount, int createSessionRetryCount, int createQueueRetryCount, int createProducerRetryCount, int createMessageRetryCount,
                              int sendRetryCount, long sleepBetweenRetryInSeconds) {
        this.createConnectionRetryCount = createConnectionRetryCount;
        this.createSessionRetryCount = createSessionRetryCount;
        this.createQueueRetryCount = createQueueRetryCount;
        this.createProducerRetryCount = createProducerRetryCount;
        this.createMessageRetryCount = createMessageRetryCount;
        this.sendRetryCount = sendRetryCount;
        this.sleepBetweenRetryInSeconds = sleepBetweenRetryInSeconds;
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

    public long getSleepBetweenRetryInSeconds() {
        return sleepBetweenRetryInSeconds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("createConnectionRetryCount", createConnectionRetryCount)
            .append("createSessionRetryCount", createSessionRetryCount)
            .append("createQueueRetryCount", createQueueRetryCount)
            .append("createProducerRetryCount", createProducerRetryCount)
            .append("createMessageRetryCount", createMessageRetryCount)
            .append("sendRetryCount", sendRetryCount)
            .append("sleepBetweenRetryInSeconds", sleepBetweenRetryInSeconds)
            .toString();
    }
}

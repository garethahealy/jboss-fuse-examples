/*
 * #%L
 * activemq-client-poc
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
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

import javax.jms.Session;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BrokerConfiguration {

    private String brokerURL;
    private String username;
    private String password;
    private int maxConnections;
    private int maximumActiveSessionPerConnection;
    private boolean isTransacted;
    private int acknowledgeMode;

    public BrokerConfiguration() {
        this("tcp://0.0.0.0:61616", "admin", "admin", 8, 100, false, Session.AUTO_ACKNOWLEDGE);
    }

    public BrokerConfiguration(String brokerURL, String username, String password, int maxConnections, int maximumActiveSessionPerConnection, boolean isTransacted,
                               int acknowledgeMode) {
        this.brokerURL = brokerURL;
        this.username = username;
        this.password = password;
        this.maxConnections = maxConnections;
        this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
        this.isTransacted = isTransacted;
        this.acknowledgeMode = acknowledgeMode;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getMaximumActiveSessionPerConnection() {
        return maximumActiveSessionPerConnection;
    }

    public boolean isTransacted() {
        return isTransacted;
    }

    public int getAcknowledgeMode() {
        return acknowledgeMode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("brokerURL", brokerURL)
            .append("username", username)
            .append("password", password)
            .append("maxConnections", maxConnections)
            .append("maximumActiveSessionPerConnection", maximumActiveSessionPerConnection)
            .append("isTransacted", isTransacted)
            .append("acknowledgeMode", acknowledgeMode)
            .append("transacted", isTransacted())
            .toString();
    }
}

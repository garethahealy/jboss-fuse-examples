package com.garethahealy.activemq.client.poc.services;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.junit.After;
import org.junit.Before;

import java.net.URI;

public abstract class BaseBroker {

        protected BrokerService broker;

        protected void startBroker() throws Exception {
                broker = new BrokerService();
                broker.setPersistent(false);

                TransportConnector connector = broker.addConnector(new TransportConnector());
                connector.setUri(new URI("tcp://0.0.0.0:61616"));
                connector.setName("tcp");

                broker.start();
                broker.waitUntilStarted();
        }

        protected void stopBroker() throws Exception {
                if (broker != null) {
                        broker.stop();
                        broker.waitUntilStopped();
                        broker = null;
                }
        }
}

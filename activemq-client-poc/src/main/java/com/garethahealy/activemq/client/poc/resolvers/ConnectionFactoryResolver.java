package com.garethahealy.activemq.client.poc.resolvers;

import javax.jms.ConnectionFactory;

public interface ConnectionFactoryResolver {

        ConnectionFactory start();

        void stop();
}

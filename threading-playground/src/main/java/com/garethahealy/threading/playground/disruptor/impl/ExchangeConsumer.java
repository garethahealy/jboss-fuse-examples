package com.garethahealy.threading.playground.disruptor.impl;

import com.garethahealy.threading.playground.disruptor.ExchangeEventConsumer;
import com.garethahealy.threading.playground.executorservice.ExampleThreading;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeConsumer implements ExchangeEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleThreading.class);

    public void onEvent(Exchange event, long sequence, boolean endOfBatch) {
        LOG.info("Event: " + event);
    }
}

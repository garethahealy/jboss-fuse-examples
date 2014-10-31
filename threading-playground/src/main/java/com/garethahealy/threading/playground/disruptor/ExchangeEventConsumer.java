package com.garethahealy.threading.playground.disruptor;

import org.apache.camel.Exchange;

import com.lmax.disruptor.EventHandler;

public interface ExchangeEventConsumer extends EventHandler<Exchange> {

}

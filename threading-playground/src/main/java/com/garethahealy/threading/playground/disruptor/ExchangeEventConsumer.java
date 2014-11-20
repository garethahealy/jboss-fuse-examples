package com.garethahealy.threading.playground.disruptor;

import com.lmax.disruptor.EventHandler;
import org.apache.camel.Exchange;

public interface ExchangeEventConsumer extends EventHandler<Exchange> {

}

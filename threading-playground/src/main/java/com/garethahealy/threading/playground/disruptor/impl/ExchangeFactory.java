package com.garethahealy.threading.playground.disruptor.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

import com.lmax.disruptor.EventFactory;

public class ExchangeFactory implements EventFactory<Exchange> {

	private CamelContext camelContext;
	
	public ExchangeFactory(CamelContext camelContext) {
		this.camelContext = camelContext;
	}
	
	@Override
	public Exchange newInstance() {
		return new DefaultExchange(camelContext);
	}
}

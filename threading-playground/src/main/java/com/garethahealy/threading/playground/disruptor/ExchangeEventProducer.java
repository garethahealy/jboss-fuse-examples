package com.garethahealy.threading.playground.disruptor;

import org.apache.camel.Exchange;

import com.lmax.disruptor.RingBuffer;

public interface ExchangeEventProducer<T> {

	void setRingBuffer(final RingBuffer<Exchange> ringBuffer);
	
	void onData(T body);
	
	void stop();
}

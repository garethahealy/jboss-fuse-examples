package com.garethahealy.threading.playground.disruptor.impl;

import java.nio.ByteBuffer;

import org.apache.camel.Exchange;

import com.garethahealy.threading.playground.disruptor.ExchangeEventProducer;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class ExchangeProducer implements ExchangeEventProducer<ByteBuffer> {
	
	private boolean isStopping;
	private RingBuffer<Exchange> ringBuffer;

	public void setRingBuffer(final RingBuffer<Exchange> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	private static final EventTranslatorOneArg<Exchange, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<Exchange, ByteBuffer>() {
		public void translateTo(Exchange event, long sequence, ByteBuffer bb) {
			event.getIn().setBody(bb.getLong(0)); // Fill with data
		}
	};

	@Override
	public void onData(ByteBuffer bb) {
		if (isStopping) {
			throw new IllegalStateException("isStopping == true");
		}
		
		ringBuffer.publishEvent(TRANSLATOR, bb);
	}
	
	@Override
	public void stop() {
		this.isStopping = true;
	}
}

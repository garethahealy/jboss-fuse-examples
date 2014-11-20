package com.garethahealy.threading.playground.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.apache.camel.Exchange;

public interface ExchangeEventProducer<T> {

        void setRingBuffer(final RingBuffer<Exchange> ringBuffer);

        void onData(T body);

        void stop();
}

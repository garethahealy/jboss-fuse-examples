package com.garethahealy.threading.playground.disruptor.impl;

import com.garethahealy.threading.playground.disruptor.ExchangeEventProducer;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import org.apache.camel.Exchange;

import java.nio.ByteBuffer;

public class ExchangeProducer implements ExchangeEventProducer<ByteBuffer> {

        private static final EventTranslatorOneArg<Exchange, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<Exchange, ByteBuffer>() {
                public void translateTo(Exchange event, long sequence, ByteBuffer bb) {
                        event.getIn().setBody(bb.getLong(0)); // Fill with data
                }
        };
        private boolean isStopping;
        private RingBuffer<Exchange> ringBuffer;

        public void setRingBuffer(final RingBuffer<Exchange> ringBuffer) {
                this.ringBuffer = ringBuffer;
        }

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

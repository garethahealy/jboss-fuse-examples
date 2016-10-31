/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: Threading Playground
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.threading.playground.disruptor.impl;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.garethahealy.threading.playground.disruptor.ExchangeEventProducer;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import org.apache.camel.Exchange;

public class ExchangeProducer implements ExchangeEventProducer<ByteBuffer> {

    private static final EventTranslatorOneArg<Exchange, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<Exchange, ByteBuffer>() {
        public void translateTo(Exchange event, long sequence, ByteBuffer bb) {
            event.getIn().setBody(bb.getLong(0));
        }
    };

    private AtomicBoolean isStopping = new AtomicBoolean();
    private RingBuffer<Exchange> ringBuffer;

    public void setRingBuffer(final RingBuffer<Exchange> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer body) throws IllegalStateException {
        if (isStopping.get()) {
            throw new IllegalStateException("isStopping == true");
        }

        if (ringBuffer == null) {
            throw new IllegalStateException("ringBuffer == null");
        }

        ringBuffer.publishEvent(TRANSLATOR, body);
    }

    @Override
    public void stop() {
        isStopping.set(true);
    }
}

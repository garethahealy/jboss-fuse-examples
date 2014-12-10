/*
 * #%L
 * threading-playground
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.garethahealy.threading.playground.disruptor.ExchangeEventConsumer;
import com.garethahealy.threading.playground.disruptor.ExchangeEventProducer;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisruptorService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DisruptorService.class);

    private final int shutdownTimeout = 1;

    private Disruptor<Exchange> disruptor;
    private int bufferSize;
    private EventFactory<Exchange> factory;
    private ExecutorService executorService;
    private List<ExchangeEventProducer<T>> producers;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public EventFactory<Exchange> getFactory() {
        return factory;
    }

    public void setFactory(EventFactory<Exchange> factory) {
        this.factory = factory;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Disruptor<Exchange> getDisruptor() {
        return disruptor;
    }

    public void init() throws NullPointerException {
        if (factory == null) {
            throw new NullPointerException("factory == null");
        }

        if (bufferSize <= 0) {
            throw new NullPointerException("bufferSize <= 0");
        }

        if (executorService == null) {
            throw new NullPointerException("executorService == null");
        }

        producers = new ArrayList<ExchangeEventProducer<T>>();
        disruptor = new Disruptor<Exchange>(factory, bufferSize, executorService, ProducerType.SINGLE, new YieldingWaitStrategy());
    }

    @SuppressWarnings("unchecked")
    public void registerConsumer(ExchangeEventConsumer consumer) throws IllegalStateException, NullPointerException {
        if (disruptor == null) {
            throw new IllegalStateException("disruptor == null, call init");
        }

        if (consumer == null) {
            throw new NullPointerException("consumer == null");
        }

        disruptor.handleEventsWith(consumer);
    }

    public void registerProducer(ExchangeEventProducer<T> producer) throws IllegalStateException, NullPointerException {
        if (disruptor == null) {
            throw new IllegalStateException("disruptor == null, call init");
        }

        if (producer == null) {
            throw new NullPointerException("consumer == null");
        }

        producer.setRingBuffer(disruptor.getRingBuffer());

        producers.add(producer);
    }

    public void start() throws IllegalStateException {
        if (disruptor == null) {
            throw new IllegalStateException("disruptor == null, call init");
        }

        disruptor.start();
    }

    public void shutdownGracefully() throws IllegalStateException {
        if (disruptor == null) {
            throw new IllegalStateException("disruptor == null, call init");
        }

        for (ExchangeEventProducer<T> producer : producers) {
            producer.stop();
        }

        try {
            disruptor.shutdown(shutdownTimeout, TimeUnit.MINUTES);
        } catch (TimeoutException ex) {
            LOG.error(ex.getMessage());
        }
    }
}

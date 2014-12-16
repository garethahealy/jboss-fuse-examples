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

public class LongEventMain {
/**
 public void doWork() throws TimeoutException {
 // Executor that will be used to construct new threads for consumers
 Executor executor = Executors.newCachedThreadPool();

 // The factory for the event
 ExchangeFactory factory = new ExchangeFactory();

 // Specify the size of the ring buffer, must be power of 2.
 int bufferSize = 1024;

 // Construct the Disruptor
 Disruptor<Exchange> disruptor = new Disruptor<>(factory, bufferSize, executor);

 // Connect the handler
 disruptor.handleEventsWith(new ExchangeConsumer());

 // Start the Disruptor, starts all threads running
 disruptor.start();

 // Get the ring buffer from the Disruptor to be used for publishing.
 RingBuffer<Exchange> ringBuffer = disruptor.getRingBuffer();

 ExchangeProducer producer = null;//new ExchangeProducer(ringBuffer);

 ByteBuffer bb = ByteBuffer.allocate(8);
 for (long l = 0; true; l++) {
 bb.putLong(0, l);
 producer.onData(bb);
 }

 disruptor.shutdown(10, TimeUnit.SECONDS);
 }
 */
}

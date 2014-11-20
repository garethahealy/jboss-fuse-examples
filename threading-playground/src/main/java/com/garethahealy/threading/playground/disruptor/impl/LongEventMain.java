package com.garethahealy.threading.playground.disruptor.impl;

import com.lmax.disruptor.TimeoutException;

public class LongEventMain {

        public void doWork() throws TimeoutException {
//		// Executor that will be used to construct new threads for consumers
//		Executor executor = Executors.newCachedThreadPool();
//
//		// The factory for the event
//		ExchangeFactory factory = new ExchangeFactory();
//
//		// Specify the size of the ring buffer, must be power of 2.
//		int bufferSize = 1024;
//
//		// Construct the Disruptor
//		Disruptor<Exchange> disruptor = new Disruptor<>(factory, bufferSize, executor);
//
//		// Connect the handler
//		disruptor.handleEventsWith(new ExchangeConsumer());
//
//		// Start the Disruptor, starts all threads running
//		disruptor.start();
//		
//		// Get the ring buffer from the Disruptor to be used for publishing.
//		RingBuffer<Exchange> ringBuffer = disruptor.getRingBuffer();
//
//		ExchangeProducer producer = new ExchangeProducer(ringBuffer);
//		
//		ByteBuffer bb = ByteBuffer.allocate(8);
//		for (long l = 0; true; l++) {
//			bb.putLong(0, l);
//			producer.onData(bb);
//		}
//		
                //disruptor.shutdown(10, TimeUnit.SECONDS);

        }
}

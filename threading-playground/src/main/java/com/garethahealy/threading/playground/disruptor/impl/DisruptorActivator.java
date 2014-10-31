package com.garethahealy.threading.playground.disruptor.impl;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import org.apache.camel.Exchange;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		DisruptorService<ByteBuffer> service = new DisruptorService<ByteBuffer>();
		service.setBufferSize(1024);
		service.setExecutorService(Executors.newCachedThreadPool());
		//service.setFactory(new LongEventFactory());
		service.registerConsumer(new ExchangeConsumer());
		service.init();
		
		service.start();

		context.registerService(DisruptorService.class.getCanonicalName(), service, null);
		

		//service.setFactory(new ExchangeFactory(null)); //CamelContext
		//service.registerProducer(new ExchangeProducer());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//disruptor.shutdown(10, TimeUnit.SECONDS);
	}

}

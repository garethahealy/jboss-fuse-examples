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

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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
        DisruptorService disruptor = (DisruptorService)context.getServiceReference(DisruptorService.class);
        disruptor.shutdownGracefully();
    }

}

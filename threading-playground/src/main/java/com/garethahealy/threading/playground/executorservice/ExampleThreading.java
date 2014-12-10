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
package com.garethahealy.threading.playground.executorservice;

import org.apache.camel.Exchange;
import org.apache.camel.processor.DefaultExchangeFormatter;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ExampleThreading {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleThreading.class);

    public void doSingleThread(Exchange exchange) {
        //org.apache.camel.impl.DefaultExecutorServiceManager - TRACE

        ExecutorServiceManager manager = exchange.getContext().getExecutorServiceManager();
        ExecutorService executorService = manager.newSingleThreadExecutor(this, "ExampleSingleThreadPool");
        executorService.execute(new Runnable() {
            public void run() {
                LOG.info("Running....");
            }
        });

        manager.shutdownGraceful(executorService);
    }

    public void doThreadPool(Exchange exchange) throws InterruptedException, ExecutionException {
        List<MyIntegerCallable> callables = new ArrayList<MyIntegerCallable>();
        for (int i = 0; i < 1000; i++) {
            callables.add(new MyIntegerCallable(i));
        }

        LOG.info("About to invokeAll");
        ExecutorServiceManager manager = exchange.getContext().getExecutorServiceManager();
        ExecutorService pooledExecutorService = manager.newThreadPool(this, "ExampleThreadPoolFor200", 200, 1000);
        List<Future<Integer>> results = pooledExecutorService.invokeAll(callables);

        LOG.info("invokeAll complete");
        for (Future<Integer> result : results) {
            LOG.info("Result is... " + result.get());
        }

        manager.shutdownGraceful(pooledExecutorService);
    }

    public void logMessageHistory(Exchange exchange) {
        LOG.info(MessageHelper.dumpMessageHistoryStacktrace(exchange, new DefaultExchangeFormatter(), false));
    }
}

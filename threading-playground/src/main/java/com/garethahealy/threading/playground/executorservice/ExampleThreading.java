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

package com.garethahealy.threading.playground.executorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class MyIntegerCallable implements Callable<Integer> {

        private static final Logger LOG = LoggerFactory.getLogger(MyIntegerCallable.class);

        private Integer current;

        public MyIntegerCallable(Integer current) {
                this.current = current;
        }

        @Override
        public Integer call() throws Exception {
                LOG.info("Running...." + current);
                return current;
        }
}

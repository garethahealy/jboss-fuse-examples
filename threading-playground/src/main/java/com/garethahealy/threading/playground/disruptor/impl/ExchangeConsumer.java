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

import com.garethahealy.threading.playground.disruptor.ExchangeEventConsumer;
import com.garethahealy.threading.playground.executorservice.ExampleThreading;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeConsumer implements ExchangeEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleThreading.class);

    public void onEvent(Exchange event, long sequence, boolean endOfBatch) {
        LOG.info("Event: " + event);
    }
}

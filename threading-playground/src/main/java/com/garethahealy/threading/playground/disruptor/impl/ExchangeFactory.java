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

import com.lmax.disruptor.EventFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

public class ExchangeFactory implements EventFactory<Exchange> {

    private CamelContext camelContext;

    public ExchangeFactory(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public Exchange newInstance() {
        return new DefaultExchange(camelContext);
    }
}

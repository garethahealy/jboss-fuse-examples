/*
 * #%L
 * ws-security-jaas
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
package com.garethahealy.camelcontext.xml.routes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ToDefinition;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = CamelSpringDelegatingTestContextLoader.class, locations = {"classpath:/beans.xml"})
@UseAdviceWith
public class OtherTest {

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Autowired
    protected CamelContext camelContext;

    @Before
    public void before() throws Exception {
        ModelCamelContext context = (ModelCamelContext)camelContext;
        context.getRouteDefinition("myRouteWithin").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:start");
                weaveByToString("To[direct:test]").replace().to("mock:result");
            }
        });

        context.start();
    }

    @Test
    public void camelContextIsNotNull() throws Exception {
        final Map<String, Objects> headers = new HashMap<String, Objects>();
        final Object body = "hello";

        Processor processor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message in = exchange.getIn();
                in.setBody(body);
                if (headers != null) {
                    in.getHeaders().putAll(headers);
                }
            }
        };

        Exchange sent = template.send(processor);

        Assert.assertNotNull(sent.getIn().getBody(String.class));
        Assert.assertEquals("test", sent.getIn().getBody(String.class));
    }
}

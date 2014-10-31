package com.garethahealy.camel.activemq.transacted.routes;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends BaseCamelBlueprintTestSupport {

    @Test
    public void camelContextIsNotNull() {
        Assert.assertNotNull(context);
    }
    
    public void test() throws Exception {
    	context.getRouteDefinition("").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				weaveByType(null);
				defaultErrorHandler();
				
			}
    	});
    }
}

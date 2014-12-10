package com.garethahealy.camel.activemq.transacted.routes;

import org.junit.Test;

public class CamelContextTest extends BaseCamelBlueprintTestSupport {

    @Test
    public void camelContextIsNotNull() {
        assertNotNull(context);
    }
}

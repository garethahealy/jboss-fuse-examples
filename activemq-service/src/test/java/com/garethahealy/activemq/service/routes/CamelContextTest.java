package com.garethahealy.activemq.service.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/activemq-service-context.xml";
    }

    @Test
    public void camelContextIsNotNull() {
        Assert.assertNotNull(context);
    }
}

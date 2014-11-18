package com.garethahealy.databaseplayground.database.repositories.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/database-repositories-context.xml";
    }

    @Test
    public void camelContextIsNotNull() {
        Assert.assertNotNull(context);
    }
    
    @Override
    protected CamelContext createCamelContext() throws Exception {
    	//NOTE: This blueprints file doesnt have a camelContext, so we provide a fake one so the tests will run
    	return new DefaultCamelContext(createRegistry());
    }
}

package com.garethahealy.databaseplayground.database.datasource.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.transaction.TransactionManager;
import java.util.Dictionary;
import java.util.Map;

public class CamelContextTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/database-datasource-context.xml";
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

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);

        services.put(TransactionManager.class.getCanonicalName(), asService(transactionManager, null));
    }
}

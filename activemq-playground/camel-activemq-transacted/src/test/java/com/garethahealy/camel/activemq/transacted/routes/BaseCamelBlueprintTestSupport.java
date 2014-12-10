package com.garethahealy.camel.activemq.transacted.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

import javax.jms.ConnectionFactory;
import java.util.Dictionary;
import java.util.Map;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-activemq-transacted-context.xml";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        ConnectionFactory amqPooledConnectionFactory = Mockito.mock(ConnectionFactory.class);

        services.put(ConnectionFactory.class.getCanonicalName(), asService(amqPooledConnectionFactory, null));
    }
}

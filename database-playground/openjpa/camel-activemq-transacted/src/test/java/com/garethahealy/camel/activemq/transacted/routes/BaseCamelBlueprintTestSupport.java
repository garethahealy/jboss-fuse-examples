package com.garethahealy.camel.activemq.transacted.routes;

import java.util.Dictionary;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-activemq-transacted-context.xml";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        ConnectionFactory amqPooledConnectionFactory = Mockito.mock(ConnectionFactory.class);
        
        services.put(ConnectionFactory.class.getCanonicalName(), asService(amqPooledConnectionFactory,  null));
    }
}

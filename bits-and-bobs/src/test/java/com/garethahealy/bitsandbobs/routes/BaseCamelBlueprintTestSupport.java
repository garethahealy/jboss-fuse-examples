package com.garethahealy.bitsandbobs.routes;

import java.util.Dictionary;
import java.util.Map;

import javax.management.MBeanServer;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/bits-and-bobs-context.xml";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        MBeanServer mBeanServer = Mockito.mock(MBeanServer.class);

        services.put(MBeanServer.class.getCanonicalName(), asService(mBeanServer, null));
    }
}

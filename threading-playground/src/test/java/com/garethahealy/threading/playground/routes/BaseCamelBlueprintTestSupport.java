package com.garethahealy.threading.playground.routes;

import java.util.Dictionary;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/threading-playground-context.xml";
    }
}

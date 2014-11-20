package com.garethahealy.wssecurity.jaas.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

        @Override
        protected String getBlueprintDescriptor() {
                return "OSGI-INF/blueprint/wssecurity-jaas-context.xml";
        }
}

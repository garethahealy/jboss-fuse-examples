package com.garethahealy.optaplanner.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camel-optaplanner-context.xml";
    }
}

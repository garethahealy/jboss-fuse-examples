package com.garethahealy.wssecurity.https.cxf.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/wssecurity-https-cxf-context.xml";
    }
}

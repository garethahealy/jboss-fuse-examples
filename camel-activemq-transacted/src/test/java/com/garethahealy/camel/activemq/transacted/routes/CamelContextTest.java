package com.garethahealy.camel.activemq.transacted.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends CamelBlueprintTestSupport {

	@Override
	protected String getBlueprintDescriptor() {
		return "OSGI-INF/blueprint/camel-activemq-transacted-context.xml";
	}
	
	@Test
	public void camelContextIsNotNull() {
		Assert.assertNotNull(context);
	}
}

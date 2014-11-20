package com.garethahealy.wssecurity.jaas.routes;

import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends BaseCamelBlueprintTestSupport {

        @Test
        public void camelContextIsNotNull() {
                Assert.assertNotNull(context);
        }
}

package com.garethahealy.databaseplayground.database.cameljpa.routes;

import org.junit.Assert;
import org.junit.Test;

public class CamelContextTest extends BaseCamelBlueprintTestSupport {

        @Test
        public void camelContextIsNotNull() {
                Assert.assertNotNull(context);
        }
}

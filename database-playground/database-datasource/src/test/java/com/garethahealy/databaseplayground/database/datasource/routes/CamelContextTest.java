/*
 * #%L
 * database-datasource
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.databaseplayground.database.datasource.routes;

import java.util.Dictionary;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CamelContextTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/database-datasource-context.xml";
    }

    @Test
    public void camelContextIsNotNull() {
        Assert.assertNotNull(context);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        //NOTE: This blueprints file doesnt have a camelContext, so we provide a fake one so the tests will run
        return new DefaultCamelContext(createRegistry());
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);

        services.put(TransactionManager.class.getCanonicalName(), asService(transactionManager, null));
    }
}

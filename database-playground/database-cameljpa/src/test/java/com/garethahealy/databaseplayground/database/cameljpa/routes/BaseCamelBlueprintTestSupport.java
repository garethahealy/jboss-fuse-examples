/*
 * #%L
 * database-cameljpa
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
package com.garethahealy.databaseplayground.database.cameljpa.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Dictionary;
import java.util.Map;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/database-cameljpa-context.xml";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        PlatformTransactionManager platformTransactionManager = Mockito.mock(PlatformTransactionManager.class);
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);

        provideMockMethods(platformTransactionManager);
        provideMockMethods(entityManagerFactory);

        services.put(PlatformTransactionManager.class.getCanonicalName(), asService(platformTransactionManager, null));
        services.put(EntityManagerFactory.class.getCanonicalName(), asService(entityManagerFactory, "osgi.unit.name", "playground-persistence"));
    }

    protected void provideMockMethods(EntityManagerFactory emf) {

    }

    protected void provideMockMethods(PlatformTransactionManager ptm) {

    }
}


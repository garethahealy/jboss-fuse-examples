/*
 * #%L
 * database-repositories
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
package com.garethahealy.databaseplayground.database.repositories.routes;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

public class BaseCamelBlueprintTestSupport extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/database-repositories-context.xml";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);

        provideMockMethods(transactionManager);
        provideMockMethods(entityManagerFactory);

        services.put(TransactionManager.class.getCanonicalName(), asService(transactionManager, null));
        services.put(EntityManagerFactory.class.getCanonicalName(), asService(entityManagerFactory, getEntityManagerFactoryDictionary()));
    }

    private Dictionary getEntityManagerFactoryDictionary() {
        Dictionary entityManagerFactoryDictionary = new Hashtable();
        // entityManagerFactoryDictionary.put("org.apache.aries.jpa.proxy.factory", true);
        //entityManagerFactoryDictionary.put("org.apache.aries.jpa.proxy.factory", "*");
        entityManagerFactoryDictionary.put("osgi.unit.name", "playground-persistence");

        return entityManagerFactoryDictionary;
    }

    protected void provideMockMethods(EntityManagerFactory emf) {

    }

    protected void provideMockMethods(TransactionManager tm) {

    }
}


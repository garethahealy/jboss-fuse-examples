/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: Database Playground :: Camel JPA
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CamelContextTest extends BaseCamelBlueprintTestSupport {

    @Override
    protected void provideMockMethods(EntityManagerFactory emf) {
        Mockito.when(emf.createEntityManager()).thenReturn(Mockito.mock(EntityManager.class));
    }

    @Test
    public void camelContextIsNotNull() {
        Assert.assertNotNull(context);
    }
}

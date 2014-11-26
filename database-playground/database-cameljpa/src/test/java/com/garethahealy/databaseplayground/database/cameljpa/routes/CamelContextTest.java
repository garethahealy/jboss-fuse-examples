package com.garethahealy.databaseplayground.database.cameljpa.routes;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

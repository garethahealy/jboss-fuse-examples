package com.garethahealy.databaseplayground.database.repositories.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

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


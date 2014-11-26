package com.garethahealy.databaseplayground.database.cameljpa.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
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

                services.put(PlatformTransactionManager.class.getCanonicalName(), asService(platformTransactionManager, null));
                services.put(EntityManagerFactory.class.getCanonicalName(), asService(entityManagerFactory, "osgi.unit.name", "playground-persistence"));
        }
}


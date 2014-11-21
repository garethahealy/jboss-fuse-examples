package com.garethahealy.databaseplayground.database.cameljpa.routes;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.mockito.Mockito;

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
                DataSource mysqlBasicManagedDataSource = Mockito.mock(DataSource.class);

                services.put(DataSource.class.getCanonicalName(), asService(mysqlBasicManagedDataSource, "osgi.jndi.service.name", "jdbc/mysqlBasicManagedDataSource"));
        }
}


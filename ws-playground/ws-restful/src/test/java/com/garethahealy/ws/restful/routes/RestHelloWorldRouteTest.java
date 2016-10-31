/*
 * #%L
 * GarethHealy :: JBoss Fuse Examples :: WS Playground :: WS Restful
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
package com.garethahealy.ws.restful.routes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestHelloWorldRouteTest extends BaseCamelBlueprintTestSupport {

    //https://github.com/apache/servicemix/blob/master/examples/camel/camel-cxf-rest/camel-cxf-rest-client/src/main/java/org/apache/servicemix/examples/camel/rest/client/Client.java

    private static final Logger LOG = LoggerFactory.getLogger(RestHelloWorldRouteTest.class);

    private static final String SERVICE_URL = "http://localhost:9001/rest/helloworld";

    private HttpURLConnection connect(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        return connection;
    }

    @Test
    public void sayHelloReturns() throws Exception {
        HttpURLConnection connection = null;

        try {
            connection = connect(SERVICE_URL + "/default/sayHello/" + "bob");
            connection.setDoInput(true);

            System.out.println("URL: " + connection.getURL().toString());

            InputStream stream = connection.getResponseCode() / 100 == 2
                ? connection.getInputStream()
                : connection.getErrorStream();

            String response = IOUtils.toString(stream);

            System.out.println("Status: " + connection.getResponseCode() + " " + connection.getResponseMessage());
            System.out.println("Response: " + response);

            Assert.assertEquals(new Integer(200), new Integer(connection.getResponseCode()));
            Assert.assertNotNull(response);
            Assert.assertTrue(response.length() > 0);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}


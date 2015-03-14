/*
 * #%L
 * ws-restful
 * %%
 * Copyright (C) 2013 - 2015 Gareth Healy
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

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.garethahealy.helloworld.HelloWorldRequest;

import org.junit.Assert;
import org.junit.Test;

public class RestHelloWorldRouteTest extends BaseCamelBlueprintTestSupport {

    private static final String SERVICE_URL = "http://localhost:9001/rest/helloworld";

    @Test
    public void postPerson() throws Exception {
        HelloWorldRequest request = new HelloWorldRequest();
        request.setHello("bob");

        System.out.println("\n### POST PERSON -> ");

        HttpURLConnection connection = connect(SERVICE_URL + "/default/sayHello");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/xml");

        System.out.println(connection.getURL().toString());

        JAXBContext jaxbContext = JAXBContext.newInstance(HelloWorldRequest.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // pretty xml output
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(request, System.out);
        jaxbMarshaller.marshal(request, connection.getOutputStream());

        System.out.println("\n### POST PERSON RESPONSE");
        System.out.println("Status: " + connection.getResponseCode() + " " + connection.getResponseMessage());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        Assert.assertEquals("200", connection.getResponseCode());
    }

    private HttpURLConnection connect(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        return connection;
    }
}


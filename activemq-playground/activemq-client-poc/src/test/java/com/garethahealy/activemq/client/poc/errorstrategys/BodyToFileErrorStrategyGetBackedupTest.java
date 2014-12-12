/*
 * #%L
 * activemq-client-poc
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
package com.garethahealy.activemq.client.poc.errorstrategys;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BodyToFileErrorStrategyGetBackedupTest {

    private String rootDirectory = "/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/activemq-playground/activemq-client-poc/target";

    @Test
    public void getBackedupLinesWithNoFiles() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandle";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        List<String[]> answer =  strategy.getBackedupLines("NoFile");

        Assert.assertNotNull(answer);
        Assert.assertEquals(0, answer.size());
    }
}

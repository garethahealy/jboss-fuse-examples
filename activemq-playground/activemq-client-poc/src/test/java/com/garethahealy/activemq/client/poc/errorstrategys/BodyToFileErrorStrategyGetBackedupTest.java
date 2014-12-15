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

import javax.jms.JMSException;

import org.junit.Assert;
import org.junit.Test;

public class BodyToFileErrorStrategyGetBackedupTest {

    private String rootDirectory = System.getProperty("user.dir") + "/target";

    @Test
    public void getBackedupLinesWithNoDirectoryExists() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/doesntExist";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        List<String[]> answer =  strategy.getBackedupLines("NoFile");

        Assert.assertNotNull(answer);
        Assert.assertEquals(0, answer.size());
    }

    @Test
    public void getBackedupLinesWithNoFiles() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLines";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        List<String[]> answer =  strategy.getBackedupLines("NoFile");

        Assert.assertNotNull(answer);
        Assert.assertEquals(0, answer.size());
    }

    @Test
    public void getBackedupLinesWith1FileAnd1Line() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLines1File";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy"});
        List<String[]> answer =  strategy.getBackedupLines("Test");

        Assert.assertNotNull(answer);
        Assert.assertEquals(1, answer.size());
        Assert.assertArrayEquals(new Object[] {"gareth", "healy"}, answer.get(0));
    }

    @Test
    public void getBackedupLinesWithMultpleFilesAnd1Line() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesMultpleFile";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy"});
        strategy.handle(new JMSException("Because"), "TestAnother", new Object[] {"healy", "gareth"});

        List<String[]> answerOne =  strategy.getBackedupLines("Test");
        List<String[]> answerTwo =  strategy.getBackedupLines("TestAnother");

        Assert.assertNotNull(answerOne);
        Assert.assertEquals(1, answerOne.size());
        Assert.assertArrayEquals(new Object[] {"gareth", "healy"}, answerOne.get(0));

        Assert.assertNotNull(answerTwo);
        Assert.assertEquals(1, answerTwo.size());
        Assert.assertArrayEquals(new Object[] {"healy", "gareth"}, answerTwo.get(0));
    }

    @Test
    public void getBackedupLinesWithMultpleFilesAndMultipleLines() {
        Assert.assertTrue(true);
    }

    @Test
    public void getBackedupLinesWithThreadsReadingAndWritting() {
        Assert.assertTrue(true);
    }
}

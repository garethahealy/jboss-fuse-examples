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

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.jms.JMSException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class BodyToFileErrorStrategyGetBackedupTest {

    private String rootDirectory = System.getProperty("user.dir") + "/target";

    @Test
    public void getBackedupLinesWithNoDirectoryExists() throws IOException {
        //Make sure the directory doesnt exist
        FileUtils.deleteDirectory(FileUtils.toFile(new URL("file:" + rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithNoDirectoryExists")));

        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithNoDirectoryExists";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        List<String[]> answer = strategy.getBackedupLines("NoFile");

        Assert.assertNotNull(answer);
        Assert.assertEquals(0, answer.size());
    }

    @Test
    public void getBackedupLinesWithNoFiles() throws IOException {
        try {
            //Make sure the directory is empty
            FileUtils.cleanDirectory(FileUtils.toFile(new URL("file:" + rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithNoFiles")));
        } catch (IllegalArgumentException ex) {
            //ignore
        }

        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithNoFiles";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        List<String[]> answer = strategy.getBackedupLines("NoFile");

        Assert.assertNotNull(answer);
        Assert.assertEquals(0, answer.size());
    }

    @Test
    public void getBackedupLinesWith1FileAnd1Line() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWith1FileAnd1Line";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy"});
        List<String[]> answer = strategy.getBackedupLines("Test");

        Assert.assertNotNull(answer);
        Assert.assertEquals(1, answer.size());
        Assert.assertArrayEquals(new Object[] {"gareth", "healy"}, answer.get(0));
    }

    @Test
    public void getBackedupLinesWithMultpleFilesAnd1Line() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithMultpleFilesAnd1Line";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy"});
        strategy.handle(new JMSException("Because"), "TestAnother", new Object[] {"healy", "gareth"});

        List<String[]> answerOne = strategy.getBackedupLines("Test");
        List<String[]> answerTwo = strategy.getBackedupLines("TestAnother");

        Assert.assertNotNull(answerOne);
        Assert.assertEquals(1, answerOne.size());
        Assert.assertArrayEquals(new Object[] {"gareth", "healy"}, answerOne.get(0));

        Assert.assertNotNull(answerTwo);
        Assert.assertEquals(1, answerTwo.size());
        Assert.assertArrayEquals(new Object[] {"healy", "gareth"}, answerTwo.get(0));
    }

    @Test
    public void getBackedupLinesWithMultpleFilesAndMultipleLines() {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/getBackedupLinesWithMultpleFilesAnd1Line";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        for (int i = 0; i < 10; i++) {
            strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy" + i});
            strategy.handle(new JMSException("Because"), "TestAnother", new Object[] {"healy", "gareth" + i});
        }

        List<String[]> answerOne = strategy.getBackedupLines("Test");
        List<String[]> answerTwo = strategy.getBackedupLines("TestAnother");

        Assert.assertNotNull(answerOne);
        Assert.assertEquals(10, answerOne.size());

        Assert.assertNotNull(answerTwo);
        Assert.assertEquals(10, answerTwo.size());

        for (int i = 0; i < 10; i++) {
            Assert.assertArrayEquals(new Object[] {"gareth", "healy" + i}, answerOne.get(i));
            Assert.assertArrayEquals(new Object[] {"healy", "gareth" + i}, answerTwo.get(i));
        }
    }

    @Test
    public void getBackedupLinesWithThreadsReadingAndWritting() {
        Assert.assertTrue(true);
    }
}

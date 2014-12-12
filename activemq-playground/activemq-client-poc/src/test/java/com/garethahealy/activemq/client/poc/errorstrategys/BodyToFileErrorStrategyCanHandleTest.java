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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.jms.JMSException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Assert;
import org.junit.Test;

public class BodyToFileErrorStrategyCanHandleTest {

    private String rootDirectory = "/Users/garethah/Documents/github/garethahealy/jboss-fuse-examples/activemq-playground/activemq-client-poc/target";

    private Collection<File> getGeneratedFiles(String pathToPersistenceStore) throws MalformedURLException {
        File directory = FileUtils.toFile(new URL("file:" + pathToPersistenceStore));
        Iterator<File> iterator = FileUtils.iterateFiles(directory, FileFilterUtils.prefixFileFilter("Test"), null);

        Collection<File> files = new ArrayList<File>();
        while (iterator.hasNext()) {
            files.add(iterator.next());
        }

        return files;
    }

    private List<String[]> readFile(File file) throws IOException {
        List<String[]> lines = new ArrayList<String[]>();

        List<String> linesInFile = FileUtils.readLines(file, Charset.forName("UTF8"));
        for (String line : linesInFile) {
            String[] lineSplit = line.split(",");
            lines.add(lineSplit);
        }

        return lines;
    }

    @Test
    public void canHandle() throws IOException {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandle";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
        strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy"});

        Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
        Assert.assertNotNull(generatedFiles);
        Assert.assertEquals(1, generatedFiles.size());

        for (File current : generatedFiles) {
            BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);
            List<String[]> lines = readFile(current);

            Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
            Assert.assertEquals(1, lines.size());

            for (String[] currentLine : lines) {
                Assert.assertArrayEquals(new String[] {"gareth", "healy"}, currentLine);
            }
        }
    }

    @Test
    public void canHandleThousandFailures() throws IOException {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandleThousandFailures";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        for (int i = 0; i < 1000; i++) {
            strategy.handle(new JMSException("Because"), "Test", new Object[] {"gareth", "healy" + i});
        }

        Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
        Assert.assertNotNull(generatedFiles);
        Assert.assertEquals(1, generatedFiles.size());

        for (File current : generatedFiles) {
            BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);
            List<String[]> lines = readFile(current);

            Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
            Assert.assertEquals(1000, lines.size());

            int j = 0;
            for (String[] currentLine : lines) {
                Assert.assertArrayEquals(new String[] {"gareth", "healy" + j}, currentLine);

                j++;
            }
        }
    }

    @Test
    public void canHandleMultipleThreadsOnSameQueue() throws IOException {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandleMultipleThreads";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        ExecutorService executor = Executors.newCachedThreadPool();
        Future one = executor.submit(new HandlerRunnable(strategy, "Test", "gareth", "healy"));
        Future two = executor.submit(new HandlerRunnable(strategy, "Test", "healy", "gareth"));

        try {
            one.get(5, TimeUnit.SECONDS);
            two.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Assert.assertTrue("InterruptedException", false);
        } catch (ExecutionException ex) {
            Assert.assertTrue("ExecutionException", false);
        } catch (TimeoutException ex) {
            Assert.assertTrue("TimeoutException", false);
        }

        Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
        Assert.assertNotNull(generatedFiles);
        Assert.assertEquals(1, generatedFiles.size());

        for (File current : generatedFiles) {
            BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);
            List<String[]> lines = readFile(current);

            Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
            Assert.assertEquals(2000, lines.size());

            List<List<String[]>> linesSplit = splitLinesIntoTwo(lines);

            Assert.assertEquals(2, linesSplit.size());
            Assert.assertEquals(1000, linesSplit.get(0).size());
            Assert.assertEquals(1000, linesSplit.get(1).size());

            int j = 0;
            for (String[] currentLine : linesSplit.get(0)) {
                Assert.assertArrayEquals(new String[] {"gareth", "healy" + j}, currentLine);

                j++;
            }

            j = 0;
            for (String[] currentLine : linesSplit.get(1)) {
                Assert.assertArrayEquals(new String[] {"healy", "gareth" + j}, currentLine);

                j++;
            }
        }
    }

    private List<List<String[]>> splitLinesIntoTwo(List<String[]> lines) {
        List<String[]> fileOne = new ArrayList<String[]>();
        List<String[]> fileTwo = new ArrayList<String[]>();

        for (String[] currentLine : lines) {
            boolean isFirstFile = currentLine[0].startsWith("gareth");
            boolean added = isFirstFile ? fileOne.add(currentLine) : fileTwo.add(currentLine);
        }

        List<List<String[]>> answer = new ArrayList<List<String[]>>();
        answer.add(fileOne);
        answer.add(fileTwo);

        return answer;
    }

    @Test
    public void canHandleMultipleThreadsOnDifferenceQueue() throws IOException {
        String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandleMultipleThreadsOnDifferenceQueue";
        AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

        ExecutorService executor = Executors.newCachedThreadPool();
        Future one = executor.submit(new HandlerRunnable(strategy, "Test", "gareth", "healy"));
        Future two = executor.submit(new HandlerRunnable(strategy, "TestAnother", "healy", "gareth"));

        try {
            one.get(10, TimeUnit.SECONDS);
            two.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Assert.assertTrue("InterruptedException", false);
        } catch (ExecutionException ex) {
            Assert.assertTrue("ExecutionException", false);
        } catch (TimeoutException ex) {
            Assert.assertTrue("TimeoutException", false);
        }

        Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
        Assert.assertNotNull(generatedFiles);
        Assert.assertEquals(2, generatedFiles.size());

        for (File current : generatedFiles) {
            BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);
            List<String[]> lines = readFile(current);

            Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
            Assert.assertEquals(1000, lines.size());

            String fileName = current.getName();
            boolean isFirstFile = fileName.startsWith("Test_");
            String firsname = isFirstFile ? "gareth" : "healy";
            String surname = isFirstFile ? "healy" : "gareth";

            int j = 0;
            for (String[] currentLine : lines) {
                Assert.assertArrayEquals(new String[] {firsname, surname + j}, currentLine);

                j++;
            }
        }
    }
}

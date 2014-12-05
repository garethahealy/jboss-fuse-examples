package com.garethahealy.activemq.client.poc.producers.errorstrategys;

import com.garethahealy.activemq.client.poc.errorstrategys.AmqErrorStrategy;
import com.garethahealy.activemq.client.poc.errorstrategys.BodyToFileErrorStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.*;

public class BodyToFileErrorStrategyTest {

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

        @Test
        public void canHandle() throws IOException {
                String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandle";
                AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);
                strategy.handle(null, "Test", new Object[] {"gareth", "healy"});

                Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
                Assert.assertNotNull(generatedFiles);
                Assert.assertEquals(1, generatedFiles.size());

                for (File current : generatedFiles) {
                        BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);

                        Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
                }
        }

        @Test
        public void canHandleThousandFailures() throws IOException {
                String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandleThousandFailures";
                AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

                for (int i = 0; i < 1000; i++) {
                        strategy.handle(null, "Test", new Object[] {"gareth", "healy" + i});
                }

                Collection<File> generatedFiles = getGeneratedFiles(pathToPersistenceStore);
                Assert.assertNotNull(generatedFiles);
                Assert.assertEquals(1, generatedFiles.size());

                for (File current : generatedFiles) {
                        BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);

                        Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
                }
        }

        @Test
        public void canHandleMultipleThreadsOnSameQueue() throws MalformedURLException {
                String pathToPersistenceStore = rootDirectory + "/BodyToFileErrorStrategy/canHandleMultipleThreads";
                final AmqErrorStrategy strategy = new BodyToFileErrorStrategy(pathToPersistenceStore);

                ExecutorService executor = Executors.newCachedThreadPool();
                Future one = executor.submit(new HandlerRunnable(strategy, "Test", "gareth", "healy"));
                Future two = executor.submit(new HandlerRunnable(strategy, "Test", "healy", "gareth"));

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
                Assert.assertEquals(1, generatedFiles.size());

                for (File current : generatedFiles) {
                        BigInteger fileSize = FileUtils.sizeOfAsBigInteger(current);

                        Assert.assertTrue(fileSize.compareTo(BigInteger.ZERO) > 0);
                }
        }
}

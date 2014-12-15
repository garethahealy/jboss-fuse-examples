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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodyToFileErrorStrategy implements AmqErrorStrategy {

    //NOTE: Cases not covered:
    //      1. If file cannot be written, message is lost.
    //      2. Writes are not batched, possible performance issue with high throughput.
    //      3. File is not rotated on size, only via date.
    //      4. File is written as a simple CSV
    //      5. Locking tactic is aggressive in that it lasts for longer periods that is probably needed, but is simpler

    private static final Logger LOG = LoggerFactory.getLogger(BodyToFileErrorStrategy.class);
    private static final String HIDDEN_DIRECTORY = ".complete";

    private final ReentrantLock lock = new ReentrantLock();
    private Charset utf8Charset = Charset.forName("UTF8");
    private String pathToPersistenceStore;
    private int maxFileExistRetrys = Integer.MAX_VALUE / 2;

    public BodyToFileErrorStrategy(String pathToPersistenceStore) {
        this.pathToPersistenceStore = pathToPersistenceStore;
    }

    public List<String[]> getBackedupLines(String queueName) {
        List<String[]> answer = new ArrayList<String[]>();
        try {
            lock.lock();

            Collection<File> backupFiles = getListOfBackupFiles(queueName);

            LOG.debug("Found {} backup files", backupFiles.size());

            for (File current : backupFiles) {
                answer.addAll(readFile(current));

                moveFileToHiddenDirectory(current);
            }
        } catch (IOException ex) {
            LOG.error("Exception getting backed up lines for queue:{} because {}", queueName, ExceptionUtils.getStackTrace(ex));
        } finally {
            lock.unlock();
        }

        return answer;
    }

    @Override
    public void handle(Throwable ex, String queueName, Object[] body) {
        LOG.error("Exception producing message {} to queue:{} because {}", body, queueName, ExceptionUtils.getStackTrace(ex));

        Collection<String> lines = new ArrayList<String>();
        lines.add(String.format("%s,%s", body[0], body[1]));

        try {
            lock.lock();

            URL backupUrl = getFullPathAndFileName(queueName);
            File backupFile = FileUtils.toFile(backupUrl);

            LOG.warn("Attempting to write body {} to {}", body, backupUrl.toExternalForm());

            //Touch file so we know it exists and have permissions
            FileUtils.touch(backupFile);

            //Write to disk
            writeLinesToFile(backupFile, lines);
        } catch (IOException caughtex) {
            LOG.error("Exception handling body to persistence store for queue:{} because {}", queueName, ExceptionUtils.getStackTrace(caughtex));
        } finally {
            lock.unlock();
        }
    }

    private void writeLinesToFile(File backupFile, Collection<String> lines) throws IOException {
        FileUtils.writeLines(backupFile, utf8Charset.name(), lines, null, true);
    }

    private URL getFullPathAndFileName(String queueName) throws MalformedURLException {
        String fileUri = String.format("file:%s/%s", pathToPersistenceStore, getFileName(queueName));

        LOG.debug("Got backup file as {}", fileUri);

        return new URL(FilenameUtils.separatorsToSystem(fileUri));
    }

    private String getFileName(String queueName) {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        String date = now.toString(ISODateTimeFormat.date());

        return String.format("%s_%s.csv", queueName, date);
    }

    private Collection<File> getListOfBackupFiles(String queueName) throws IOException {
        checkBackupDirectory();

        String fileUri = String.format("file:%s", pathToPersistenceStore);
        File directory = FileUtils.toFile(new URL(FilenameUtils.separatorsToSystem(fileUri)));
        IOFileFilter filter = FileFilterUtils.prefixFileFilter(String.format("%s_", queueName), IOCase.INSENSITIVE);

        LOG.debug("Looking for backup files in {} using filter {}", fileUri, filter);

        return FileUtils.listFiles(directory, filter, null);
    }

    private void checkBackupDirectory() throws IOException {
        String fileUri = String.format("file:%s%s.amq", pathToPersistenceStore, File.separatorChar);
        File tempFile = FileUtils.toFile(new URL(FilenameUtils.separatorsToSystem(fileUri)));

        LOG.debug("About to touch {}", fileUri);

        FileUtils.touch(tempFile);
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

    private void moveFileToHiddenDirectory(File source) throws IOException {
        boolean exists = true;

        int i = 0;
        while (exists) {
            File backup = FileUtils.toFile(getHiddenDirectoryPathForFile(source, String.valueOf(i)));

            try {
                FileUtils.moveFile(source, backup);
                exists = false;
            } catch (FileExistsException ex) {
                LOG.error("Exception moving file to hidden folder because {}. Retrying...", ex.getMessage());
                exists = true;
            }

            i++;

            if (i > maxFileExistRetrys) {
                throw new IOException(String.format("moveFileToHiddenDirectory has hit %s for %s" + maxFileExistRetrys, source.getName()));
            }
        }
    }

    private URL getHiddenDirectoryPathForFile(File source, String retry) throws MalformedURLException {
        String path = FilenameUtils.getFullPath(source.getAbsolutePath());
        String fileName = FilenameUtils.getName(source.getAbsolutePath());
        String fileUri = String.format("file:%s%s%s%s%s", path, HIDDEN_DIRECTORY, File.separatorChar, retry, fileName);

        LOG.debug("Got hidden directory as {}", fileUri);

        return new URL(FilenameUtils.separatorsToSystem(fileUri));
    }
}

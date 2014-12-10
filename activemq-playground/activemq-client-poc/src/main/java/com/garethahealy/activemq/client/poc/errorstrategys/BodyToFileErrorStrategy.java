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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
    //      5. No code to read in the files to re-process

    private static final Logger LOG = LoggerFactory.getLogger(BodyToFileErrorStrategy.class);

    private Charset UTF8 = Charset.forName("UTF8");
    private String pathToPersistenceStore;

    public BodyToFileErrorStrategy(String pathToPersistenceStore) {
        this.pathToPersistenceStore = pathToPersistenceStore;
    }

    @Override
    public synchronized void handle(Throwable ex, String queueName, Object[] body) {
        URL backupUrl = getFullPathAndFileName(queueName);
        File backupFile = FileUtils.toFile(backupUrl);

        Collection<String> lines = new ArrayList<String>();
        lines.add(String.format("%s,%s", body[0], body[1]));

        try {
            //Touch file so we know it exists and have permissions
            FileUtils.touch(backupFile);

            //Write to disk
            writeLinesToFile(backupFile, lines);
        } catch (IOException caughtex) {
            LOG.error("Exception handling body to persistence store {} because {}", backupUrl.toString(), ExceptionUtils.getStackTrace(caughtex));
        }

    }

    private void writeLinesToFile(File backupFile, Collection<String> lines) throws IOException {
        FileUtils.writeLines(backupFile, UTF8.name(), lines, null, true);
    }

    private URL getFullPathAndFileName(String queueName) {
        String fileUri = String.format("file:%s/%s", pathToPersistenceStore, getFileName(queueName));
        URL url = null;

        try {
            url = new URL(FilenameUtils.separatorsToSystem(fileUri));
        } catch (MalformedURLException ex) {
            LOG.error("Exception getting full path for {} because {}", fileUri, ExceptionUtils.getStackTrace(ex));
        }

        return url;
    }

    private String getFileName(String queueName) {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        String date = now.toString(ISODateTimeFormat.date());

        return String.format("%s_%s.csv", queueName, date);
    }
}

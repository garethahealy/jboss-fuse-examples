package com.garethahealy.activemq.client.poc.errorstrategys;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

public class BodyToFileErrorStrategy implements AmqErrorStrategy {

        //NOTE: Cases not covered:
        //      1. If file cannot be written, message is lost.
        //      2. Writes are not batched, possible performance issue with high throughput.
        //      3. File is not rotated on size, only via date.
        //      4. File is written as a simple CSV

        private static final Logger LOG = LoggerFactory.getLogger(BodyToFileErrorStrategy.class);

        private Charset UTF8 = Charset.forName("UTF8");
        private String pathToPersistenceStore;

        public BodyToFileErrorStrategy(String pathToPersistenceStore) {
                this.pathToPersistenceStore = pathToPersistenceStore;
        }

        @Override
        public synchronized void handle(Throwable ex, String queueName, Object[] body) throws IOException {
                URL backupUrl = getFullPathAndFileName(queueName);
                File backupFile = FileUtils.toFile(backupUrl);

                //Touch file so we know it exists
                FileUtils.touch(backupFile);

                Collection<String> lines = new ArrayList<String>();
                lines.add(String.format("%s,%s", body[0], body[1]));

                //Write to disk
                writeLinesToFile(backupFile, lines);
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

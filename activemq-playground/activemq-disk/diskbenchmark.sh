#!/usr/bin/env bash
#http://blog.christianposta.com/activemq/speeding-up-activemq-persistent-messaging-performance-by-25x/

echo RUN=java -classpath "lib/*" org.apache.activemq.store.kahadb.disk.util.DiskBenchmark

java -classpath "lib/*" org.apache.activemq.store.kahadb.disk.util.DiskBenchmark
#!/usr/bin/env bash

echo RUN=java -classpath "lib/*" org.apache.activemq.store.kahadb.disk.util.DiskBenchmark $*

java -classpath "lib/*" org.apache.activemq.store.kahadb.disk.util.DiskBenchmark $*
ActiveMQ Disk Performance
==========================
Tool to see how much writes/reads the disk supports.

How to
==========================
Copy the run.sh script to the base installation of ActiveMQ

* chmod +x run.sh
* ./run.sh

Options
==========================
* --verbose
* --bs
* --size 
* --sampleInterval

https://github.com/apache/activemq/blob/master/activemq-kahadb-store/src/main/java/org/apache/activemq/store/kahadb/disk/util/DiskBenchmark.java
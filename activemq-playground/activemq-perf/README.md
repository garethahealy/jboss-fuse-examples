# ActiveMQ Performance
POM that includes the AMQ performance plugin so we can quickly produce / consumer messages against a broker

- mvn activemq-perf:consumer -Dfactory.brokerURL=tcp://localhost:61616 -Dfactory.userName=admin -Dfactory.password=admin
- mvn activemq-perf:producer -Dfactory.brokerURL=tcp://localhost:61616 -Dfactory.userName=admin -Dfactory.password=admin -Dproducer.msgFileName=message.xml

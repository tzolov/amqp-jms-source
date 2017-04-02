# amqp-jms-source
AMQP 1.0 Spring Cloud Dataflow Source.  Uses Apache Qpid JMS AMQP 1.0 to bridge from an AMQP 1.0 messaging system to a JMS messaging system.

## Build

```bash
git clone https://github.com/tzolov/amqp-jms-source.git
cd amqp-jms-source
./mvnw clean install -PgenerateApps
```
this wil produce `apps` pom project with 3 sub-projects: 
`mqp-jms-source-kafka-09`, `amqp-jms-source-kafka-10`, `amqp-jms-source-rabbit`.

Note: In the 3 sub-project you must manually later the the `pom.xml` and replace the version 
of the `app-starters-core-dependencies` from `0.0.1-SNAPSHOT` to `1.1.1.RELEASE`. Like this:
 
```xml
      <dependency>
        <groupId>org.springframework.cloud.stream.app</groupId>
        <artifactId>app-starters-core-dependencies</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
```
 
```xml
      <dependency>
        <groupId>org.springframework.cloud.stream.app</groupId>
        <artifactId>app-starters-core-dependencies</artifactId>
        <version>1.1.1.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
```

Then build the `amqp-jms-sourc` for the tree binders. 
```bash
cd apps
mvn clean install
```
Test the `amqp-jms-source` using the `AMQP` protocol:
```bash
apps$ java -jar ./amqp-jms-source-kafka-10/target/amqp-jms-source-kafka-10-0.0.1-SNAPSHOT.jar --jms.destination=TEST  --amqp-jms-protocol=amqp  --amqp-jms-hostname=localhost --amqp-jms-port=5672 --qpid.jms.username=guest --qpid.jms.password=guest
```

Test the `amqp-jms-source` using the `AMQPS` (e.g. SSL) protocol: 
```bash
apps$ java -jar ./amqp-jms-source-kafka-10/target/amqp-jms-source-kafka-10-0.0.1-SNAPSHOT.jar --jms.destination=TEST 
--amqp-jms-protocol=amqps --amqp-jms-hostname=localhost --amqp-jms-port=5671
--qpid.jms.username=guest --qpid.jms.password=guest --qpid.transport.keyStoreLocation=path/to/certificates/client.ks --qpid.transport.keyStorePassword=<keyStorePassword> --qpid.transport.trustStoreLocation=path/to/certificates/client.ts --qpid.transport.trustStorePassword=<trustStorePassword> 
```

## Register AMQP-JMS-SOURCE
Add AMQP-JMS-SOURCE as a data source to your SCDFs applications list. 

* (optional) remove old version of amqp-jms
```bash
dataflow:>app unregister --type source --name amqp-jms
```
* Register the amqp-jms source
```bash
dataflow:>app register --type source --name amqp-jms --uri file:///…./amqp-jms-source-kafka-10-0.0.1-SNAPSHOT.jar
``` 
* Test the amqp-jms source:
```bash
dataflow:>stream create --name amqpSimple --definition "amqp-jms --qpid.jms.username=guest --qpid.jms.password=guest --destination=TEST | log" --deploy
```

## AMQPS (SSL) stream with properties file
Define stream to consume TEST queue
```bash
dataflow:>stream create --name amqpsTest --definition "amqp-jms --destination=TEST | log"
```
Create `amqps.properties` file:
```
app.amqp-jms.amqp-jms-protocol=amqps
app.amqp-jms.amqp-jms-hostname=localhost
app.amqp-jms.amqp-jms-port=5671

app.amqp-jms.qpid.jms.username=guest
app.amqp-jms.qpid.jms.password=guest

app.amqp-jms.qpid.transport.keyStoreLocation=<path/to/certificates>/client.ks
app.amqp-jms.qpid.transport.keyStorePassword=<keystore-password>
app.amqp-jms.qpid.transport.trustStoreLocation=<path/to/certificates>/client.ts
app.amqp-jms.qpid.transport.trustStorePassword=<truststore-password>
```
Note that by SCDF convention the properties start with ‘app’ prefix followed by the name of the module being configured (e.g. ‘amqp-jms’). 

Deploy with the amqps.properties file
```
dataflow:>stream deploy --name amqpsTest --propertiesFile amqps.properties
```

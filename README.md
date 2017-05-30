[![Build Status](https://travis-ci.org/Hevelian/hevelian-activemq.svg?branch=master)](https://travis-ci.org/Hevelian/hevelian-activemq) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000?style=flat-square)]()

# hevelian-activemq

**Enterprise class software for the rest of us!**
Run Apache ActiveMQ Broker as a WAR deployment in Tomcat and reduce the number of processes you need to start up.
## Build
Build project with a simple Maven command from the root folder:
```sh
mvn clean install
```

Two artifact will be generated:
1. activemq-core-`{version}`.jar - a core jar with a Broker Web Bridge abstraction
2. activemq-webapp-`{version}`.war - a Web Application to run an ActiveMQ Broker

`{version}` is equal to the version of ActiveMQ (plus **-SNAPSHOT** in case of snapshot builds).

## Run
Just deploy activemq-webapp-`{version}`.war to Tomcat and this is it. The application provides an advanced mechanism for configuration customizations, so if something is missing - find instructions below for any extensions. For the most complex cases - activemq-core-`{version}`.jar provides an abstraction to build your own web application with custom broker initialization logic.

> The Web Application just starts the ActiveMQ Broker. It does not start any UI to interact with the queues. This is planned for the next releases, but for now any third party tools can be used.

### Default
In case the war is deployed and no any additional configuration is specified - the default one will be used:

| Property| Description | Value|
| ------ | ------ | ------ |
| activemq.data | Location of ActiveMQ data folder| ${user.home}/hevelian/activemq/data |
| activemq.broker.name | ActiveMQ Broker name| Hevelian |
| activemq.broker.useJmx | Use JMX connector| true |
| activemq.broker.jmx.port | JMX connector port. Only if JMX connector is ennabled| 1099 |
| activemq.broker.tcp.port | TCP connector port| 61616 |

### Customized
Default properties can be overwritten in multiple ways:
1. ServletContext init parameters (that is, web.xml context-param entries)
2. JNDI properties
3. System properties
4. Environment variables

The overwrite order is the same as defined.

### Native
Specify custom properties in a separate configuration properties file. This approach is native since we have a single properties file for all Hevelian products for centralized configuration. The path to the configuration file can be defined through the property: `hevelian.config.path` same way as any other properties (see the previous section for details).

### Advanced
This kind of configuration requires a basic knowledge of [Spring](https://spring.io) and [Schema based configuration](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/xsd-configuration.html). The idea is to overwrite a [default](https://github.com/Hevelian/hevelian-activemq/blob/master/activemq-webapp/src/main/resources/activemq.xml) Broker bean configuration file from the classpath with your own one. To specify a different file - set an `activemq.conf.brokerURI` property to point to your own xml with bean config.
For example:
```sh
-Dactivemq.conf.brokerURI=xbean:file:///Users/myuser/activemq/conf/activemq.xml
```
More info at http://activemq.apache.org/xml-configuration.html

To change the [default](https://github.com/Hevelian/hevelian-activemq/blob/master/activemq-webapp/src/main/resources/log4j.properties) logging configuration - set the custom [Log4J](http://logging.apache.org/log4j/1.2/) config file location. E.g.
```sh
-Dlog4j.configuration=file:///Users/myuser/activemq/logging/log4j.properties
```

License
----

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

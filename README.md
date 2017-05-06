[![Build Status](https://travis-ci.org/Hevelian/hevelian-activemq.svg?branch=master)](https://travis-ci.org/Hevelian/hevelian-activemq) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000?style=flat-square)]()

# hevelian-activemq

Enterprise class software for the rest of us.

Run Apache ActiveMQ as a WAR deployment in Tomcat and reduce the number of processes you need to start up.

To run:
1. Set the location to broker configuration file. Sample: -Dactivemq.conf.brokerURI=xbean:file:///Users/yflyud/Dev/Runnables/apache-activemq-5.14.5/conf/activemq.xml
More info at http://activemq.apache.org/xml-configuration.html

2. Set the location to ActiveMQ data folder in case it is referenced by placeholder from configuration file. Sample: -Dactivemq.data=/Users/yflyud/Dev/Runnables/apache-activemq-5.14.5/data

3. Add necessary ActiveMQ libraries to Tomcat classpath. Edit the common.loader property at conf/catalina.properties. Sample: common.loader="${catalina.base}/lib","${catalina.base}/lib/*.jar","${catalina.home}/lib","${catalina.home}/lib/*.jar","${activemq.home}/lib/*.jar","${activemq.home}/lib/optional/*.jar"

NOTE: the set of libraries depends on the type and content of configuration file. Only several libraries are mandatory. List to be provided soon.

4. Edit the broker configuration file. Remove ws transport connector, import of jetty.xml. In case the activemq.conf placeholder is used - set it's value via a system property.




modify Tomcat context.xml to include one Environment parameter "hevelian.apachemq.home", for example:

&lt;Environment name="hevelian.apachemq.home" value="/hevelian/apachemq" type="java.lang.String"/&gt;

In the home folder create three folders, "conf" and "data". 
In "conf" folder create a file "hevelian.properties" and add the following to the file:

broker.adapter.store=/hevelian/activemq/data<br/>
broker.name=Hevelian<br/>
broker.connector=tcp://localhost:61616<br/>
broker.useJmx=true<br/>

Change the broker.adapter.store to point to the folder you created of course :)

[![Build Status](https://travis-ci.org/Hevelian/hevelian-activemq.svg?branch=master)](https://travis-ci.org/Hevelian/hevelian-activemq)[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000?style=flat-square)]()

# hevelian-activemq

Enterprise class software for the rest of us.

Run Apache ActiveMQ as a WAR deployment in Tomcat and reduce the number of processes you need to start up.

To run:
modify Tomcat context.xml to include one Environment parameter "hevelian.apachemq.home", for example:

&lt;Environment name="hevelian.apachemq.home" value="/hevelian/apachemq" type="java.lang.String"/&gt;

In the home folder create three folders, "conf" and "data". 
In "conf" folder create a file "hevelian.properties" and add the following to the file:

broker.adapter.store=/hevelian/activemq/data<br/>
broker.name=Hevelian<br/>
broker.connector=tcp://localhost:61616<br/>
broker.useJmx=true<br/>

Change the broker.adapter.store to point to the folder you created of course :)

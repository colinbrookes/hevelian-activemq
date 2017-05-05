# hevelian-activemq

Enterprise class software for the rest of us.

Run Apache ActiveMQ as a WAR deployment in Tomcat and reduce the number of processes you need to start up.

To run:
modify Tomcat context.xml to include one Environment parameter "hevelian.apachemq.home", for example:

&lt;Environment name="hevelian.apachemq.home" value="/hevelian/apachemq" type="java.lang.String"/&gt;

In the home folder create three folders, "conf" and "data". 
In "conf" folder create a file "hevelian.properties" and add the following to the file:

broker.adapter.store=/hevelian/activemq/data
broker.name=Hevelian
broker.connector=tcp://localhost:61616
broker.useJmx=true

Change the broker.adapter.store to point to the folder you created of course :)
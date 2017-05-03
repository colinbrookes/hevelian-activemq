package com.hevelian.activemq.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Set;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.http.HttpServletRequest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/queue.svc")
public class HevelianQueueService {
	private static final Logger logger = LogManager.getLogger(HevelianQueueService.class);

	@RequestMapping(value="/all", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getAll(HttpServletRequest request) throws UnsupportedEncodingException, IntrospectionException, InstanceNotFoundException, ReflectionException {
		logger.debug("QueueController: get all queues");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.TEXT_HTML);
		
		String beans = "<html><body>";
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objectNames = server.queryNames(null, null);
		for (ObjectName name : objectNames) {
		    MBeanInfo info = server.getMBeanInfo(name);
		    
		    beans += name.toString() + "<br/>";
		}

		beans += "</body></html>";
		
		return new ResponseEntity<byte[]>(beans.getBytes("UTF-8"), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value="/stats", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getStats(HttpServletRequest request) throws IntrospectionException, InstanceNotFoundException, ReflectionException, MalformedObjectNameException, IOException {
		logger.debug("QueueController: get all queue stats");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url);
		MBeanServerConnection conn = jmxc.getMBeanServerConnection();		
		
		ObjectName activeMQ = new ObjectName("org.apache.activemq:type=Broker,brokerName=Hevelian");
		BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ,BrokerViewMBean.class, true);
		
		System.out.println("CONNECTED: " + mbean.getCurrentConnectionsCount());
		
		for (ObjectName name : mbean.getQueues()) {
		    QueueViewMBean queueMbean = (QueueViewMBean) 
		           MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class, true);

		    System.out.println("QUEUE: " + queueMbean.getName() + ", SIZ COUNT: " + queueMbean.getQueueSize());
		    System.out.println("QUEUE: " + queueMbean.getName() + ", ENQ COUNT: " + queueMbean.getEnqueueCount());
		    System.out.println("QUEUE: " + queueMbean.getName() + ", INF COUNT: " + queueMbean.getInFlightCount());
		    System.out.println("QUEUE: " + queueMbean.getName() + ", DEQ COUNT: " + queueMbean.getDequeueCount());
		    System.out.println("QUEUE: " + queueMbean.getName() + ", DIS COUNT: " + queueMbean.getDispatchCount());
		} 
		
		return new ResponseEntity<byte[]>(mbean.getQueues().toString().getBytes("UTF-8"), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value="/test", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getTest(HttpServletRequest request) throws UnsupportedEncodingException {
		String stats = null;
		
		try {
             ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
             javax.jms.Connection connection = connectionFactory.createConnection();
             connection.start();

             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             Destination destination = session.createQueue("TEST.FOO");
             MessageProducer producer = session.createProducer(destination);
             producer.setDeliveryMode(DeliveryMode.PERSISTENT);

             String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
             TextMessage message = session.createTextMessage(text);

             System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
             producer.send(message);

             stats = connectionFactory.getStats().toString();

             session.close();
             connection.close();
         }
         catch (Exception e) {
             System.out.println("Caught: " + e);
             e.printStackTrace();
         }
		 
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<byte[]>(stats.getBytes("UTF-8"), responseHeaders, HttpStatus.OK);
		 
	}
}

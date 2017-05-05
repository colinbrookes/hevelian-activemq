package com.hevelian.activemq;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.activemq.usage.SystemUsage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hevelian.activemq.Configuration;

public class Loader extends HttpServlet {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = -8289081958495740549L;
	private static final String contextName = "hevelian.activemq.home";
	private Configuration configuration = new Configuration(contextName);

	private BrokerService broker;

	private static final Logger logger = LogManager.getLogger(Loader.class.getName());

	@Override
	public void init() throws ServletException {
		
		logger.info("Load activeMQ");
		System.out.println("Starting ActiveMQ Broker");
		// configure the broker
		try {
			
			PersistenceAdapter adapter = new KahaDBPersistenceAdapter();
			adapter.setDirectory(new File(configuration.getProperty("broker.adapter.store")));
			
			broker = new BrokerService();
			broker.addConnector(configuration.getProperty("broker.connector"));
			broker.setUseJmx(Boolean.parseBoolean(configuration.getProperty("broker.useJmx")));
			broker.setBrokerName(configuration.getProperty("broker.name"));
			broker.setPersistenceAdapter(adapter);
			
			broker.start();
			logger.info("ActiveMQ loaded succesfully");
			
			SystemUsage su = broker.getConsumerSystemUsage();
			System.out.println(su.getStoreUsage());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to load ActiveMQ!");
		}
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	public void destroy() {
		try {
			logger.info("ActiveMQ exiting");
			broker.stop();
			logger.info("ActiveMQ exit succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to exit ActiveMQ!");
		}
	}
}
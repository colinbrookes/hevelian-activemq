package com.hevelian.activemq;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class BrokerInitializer implements ServletContextListener {
	private BrokerService broker;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Starting ActiveMQ Broker");
		URI configURI;
		try {
			// TODO read the property handling the absence.
			configURI = new URI(System.getProperty("activemq.conf.brokerURI"));
		} catch (URISyntaxException e) {
			throw new BrokerLifecycleException("Unable to read broker configuration URI.", e);
		}

		// TODO use logging
		System.out.println("Loading message broker from: " + configURI);
		try {
			broker = BrokerFactory.createBroker(configURI);
			broker.start();
		} catch (Exception e) {
			throw new BrokerLifecycleException("Broker initialization failed.", e);
		}

		if (!broker.waitUntilStarted()) {
			throw new BrokerLifecycleException("Broker failed to start.", broker.getStartException());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (broker == null || !broker.isStarted()) {
			return;
		}
		System.out.println("Stopping ActiveMQ Broker");
		try {
			broker.stop();
			broker.waitUntilStopped();
		} catch (Exception e) {
			throw new BrokerLifecycleException("Broker failed to stop.", e);
		}
	}
}
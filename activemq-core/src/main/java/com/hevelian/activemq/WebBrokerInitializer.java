package com.hevelian.activemq;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebBrokerInitializer implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(WebBrokerInitializer.class);

	private BrokerService broker;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Starting ActiveMQ Broker");

		try {
			broker = createBroker(sce.getServletContext());
			broker.start();
		} catch (Exception e) {
			throw new BrokerLifecycleException("Broker initialization failed", e);
		}

		if (!broker.waitUntilStarted()) {
			throw new BrokerLifecycleException("Broker failed to start", broker.getStartException());
		}
	}

	protected abstract BrokerService createBroker(ServletContext servletContext) throws Exception;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (broker == null || !broker.isStarted()) {
			return;
		}
		LOG.info("Stopping ActiveMQ Broker");
		try {
			broker.stop();
			broker.waitUntilStopped();
		} catch (Exception e) {
			throw new BrokerLifecycleException("Broker failed to stop", e);
		}
	}
}
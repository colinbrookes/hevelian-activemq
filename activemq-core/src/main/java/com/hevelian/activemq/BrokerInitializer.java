package com.hevelian.activemq;

import java.net.URI;
import java.net.URISyntaxException;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hevelian.activemq.jndi.JndiLookup;

public class BrokerInitializer implements ServletContextListener {
	private static final String BROKER_URI_PROPERTY = "activemq.conf.brokerURI";

	private static final Logger LOG = LoggerFactory.getLogger(BrokerInitializer.class);

	private BrokerService broker;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Starting ActiveMQ Broker");
		URI configURI;
		try {
			String brokerURIProperty = getBrokerURIProperty(sce.getServletContext());
			if (brokerURIProperty == null) {
				throw new BrokerLifecycleException("Broker configuration URI not found");
			}
			configURI = new URI(brokerURIProperty);
		} catch (URISyntaxException e) {
			throw new BrokerLifecycleException("Unable to read broker configuration URI", e);
		}

		LOG.info("Loading message broker from: " + configURI);
		try {
			broker = BrokerFactory.createBroker(configURI);
			broker.start();
		} catch (Exception e) {
			throw new BrokerLifecycleException("Broker initialization failed", e);
		}

		if (!broker.waitUntilStarted()) {
			throw new BrokerLifecycleException("Broker failed to start", broker.getStartException());
		}
	}

	protected String getBrokerURIProperty(ServletContext ctx) {
		String brokerURIString = System.getProperty(BROKER_URI_PROPERTY);
		if (brokerURIString == null) {
			LOG.debug("No broker config URI found in System properties. Getting URI from JNDI context");
			try {
				brokerURIString = new JndiLookup().lookup(BROKER_URI_PROPERTY, String.class);
			} catch (NamingException e) {
				LOG.debug("No broker config URI found in JNDI context. Reason: {}. Getting URI from init parameter",
						e.getMessage());
				brokerURIString = ctx.getInitParameter(BROKER_URI_PROPERTY);
			}
		}
		return brokerURIString;
	}

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
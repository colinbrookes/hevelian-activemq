package com.hevelian.activemq;

import java.net.URI;

import javax.servlet.ServletContext;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hevelian.activemq.support.ReversePropertySourcesStandardServletEnvironment;

public class WebBrokerInitializerImpl extends WebBrokerInitializer {
	private static final String BROKER_URI_PROP = "activemq.conf.brokerURI";
	private static final Logger LOG = LoggerFactory.getLogger(WebBrokerInitializerImpl.class);

	@Override
	// TODO throw corresponding exceptions, not just Exception instances.
	protected BrokerService createBroker(ServletContext sc) throws Exception {
		StandardServletEnvironment standardServletEnvironment = new ReversePropertySourcesStandardServletEnvironment();
		WebApplicationContextUtils.initServletPropertySources(standardServletEnvironment.getPropertySources(), sc);

		URI configURI = new URI(standardServletEnvironment.getProperty(BROKER_URI_PROP));

		LOG.info("Loading message broker from: " + configURI);
		return BrokerFactory.createBroker(configURI);
	}
}
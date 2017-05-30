package com.hevelian.activemq.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Context listener that stores current Servlet Context instance to a
 * {@link ServletContextHolder}.
 * 
 * @author yflyud
 *
 */
public class ServletContextHolderInitializer implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(ServletContextHolderInitializer.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextHolder.setServletContext(sce.getServletContext());
		LOG.debug(String.format("Servlet context with name '%s' is set as current",
				sce.getServletContext().getServletContextName()));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextHolder.removeServletContext();
		LOG.debug("Current Servlet Context is set to null");

	}
}
package com.hevelian.activemq.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet Context listener that stores current Servlet Context instance to a
 * {@link ServletContextHolder}.
 * 
 * @author yflyud
 *
 */
public class ServletContextHolderInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextHolder.setServletContext(sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextHolder.removeServletContext();
	}
}
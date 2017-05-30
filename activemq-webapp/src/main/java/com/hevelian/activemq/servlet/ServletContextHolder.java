package com.hevelian.activemq.servlet;

import javax.servlet.ServletContext;

/**
 * Holder of current Servlet Context. Servlet Context instance should be set
 * from outside.
 * 
 * @author yflyud
 *
 */
public final class ServletContextHolder {
	private static ServletContext servletContext;

	private ServletContextHolder() {
	}

	public static synchronized void setServletContext(ServletContext sc) {
		if (servletContext != null) {
			throw new IllegalStateException("Servlet context is already set.");
		}
		if (sc == null) {
			throw new IllegalArgumentException("ServletContext instance cannot be null.");
		}
		servletContext = sc;
	}

	public static synchronized void removeServletContext() {
		if (servletContext == null) {
			throw new IllegalStateException("Servlet context is not set.");
		}
		servletContext = null;
	}

	public static boolean isServletContextSet() {
		return servletContext != null;
	}

	public static ServletContext getServletContext() {
		ServletContext sc = servletContext;
		if (sc == null) {
			throw new IllegalStateException("Servlet context is not set.");
		}
		return sc;
	}
}
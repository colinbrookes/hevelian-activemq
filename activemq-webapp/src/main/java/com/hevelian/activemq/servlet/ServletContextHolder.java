package com.hevelian.activemq.servlet;

import javax.servlet.ServletContext;

public class ServletContextHolder {
	private static ServletContext sc;

	public static synchronized void setServletContext(ServletContext sc) {
		if (ServletContextHolder.sc != null) {
			throw new IllegalStateException("Servlet context is already set.");
		}
		ServletContextHolder.sc = sc;
	}

	public static synchronized ServletContext getServletContext() {
		if (sc == null) {
			synchronized (ServletContextHolder.class) {
				if (sc == null) {
					throw new IllegalStateException("Servlet context is not set.");
				}
			}
		}
		return sc;
	}

}
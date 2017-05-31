package com.hevelian.activemq.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServletContextHolderInitializerTest {

	@Before
	public void clearServletContextHolder() {
		if (ServletContextHolder.isServletContextSet())
			ServletContextHolder.removeServletContext();
	}

	@Test
	public void contextInitialized() {
		ServletContextHolderInitializer i = new ServletContextHolderInitializer();
		ServletContext sc = Mockito.mock(ServletContext.class);
		i.contextInitialized(new ServletContextEvent(sc));
		assertEquals(sc, ServletContextHolder.getServletContext());
	}

	@Test
	public void contextDestroyed() {
		ServletContextHolderInitializer i = new ServletContextHolderInitializer();
		ServletContext sc = Mockito.mock(ServletContext.class);
		ServletContextHolder.setServletContext(sc);
		i.contextDestroyed(new ServletContextEvent(sc));
		assertFalse(ServletContextHolder.isServletContextSet());
	}

	@Test(expected = IllegalStateException.class)
	public void contextDestroyed_differentContext_ExcThrown() {
		ServletContextHolderInitializer i = new ServletContextHolderInitializer();
		ServletContextHolder.setServletContext(Mockito.mock(ServletContext.class));
		i.contextDestroyed(new ServletContextEvent(Mockito.mock(ServletContext.class)));
	}

}

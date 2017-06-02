package com.hevelian.activemq.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

public class ServletContextHolderTest {

	@After
	public void clearServletContextHolder() {
		if (ServletContextHolder.isServletContextSet())
			ServletContextHolder.removeServletContext();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setServletContext_NullArg_ExcThrown() {
		ServletContextHolder.setServletContext(null);
	}

	@Test(expected = IllegalStateException.class)
	public void setServletContext_contextAlreadySet_ExcThrown() {
		ServletContextHolder.setServletContext(Mockito.mock(ServletContext.class));
		ServletContextHolder.setServletContext(Mockito.mock(ServletContext.class));
	}

	@Test
	public void getAndSetServletContext() {
		ServletContext sc = Mockito.mock(ServletContext.class);
		ServletContextHolder.setServletContext(sc);
		assertEquals(sc, ServletContextHolder.getServletContext());
	}

	@Test(expected = IllegalStateException.class)
	public void getServletContext_servletContextIsNull_ExcThrown() {
		ServletContextHolder.getServletContext();
	}

	@Test
	public void removeServletContext() {
		ServletContext sc = Mockito.mock(ServletContext.class);
		ServletContextHolder.setServletContext(sc);
		ServletContextHolder.removeServletContext();
		assertFalse(ServletContextHolder.isServletContextSet());
	}

	@Test(expected = IllegalStateException.class)
	public void removeServletContext_servletContextNotSet_ExcThrown() {
		ServletContextHolder.removeServletContext();
	}

	@Test
	public void isServletContextSet() {
		ServletContext sc = Mockito.mock(ServletContext.class);
		ServletContextHolder.setServletContext(sc);
		assertTrue(ServletContextHolder.isServletContextSet());
		ServletContextHolder.removeServletContext();
		assertFalse(ServletContextHolder.isServletContextSet());
	}

}

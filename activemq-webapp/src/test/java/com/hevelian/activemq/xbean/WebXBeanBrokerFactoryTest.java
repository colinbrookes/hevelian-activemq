package com.hevelian.activemq.xbean;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import com.hevelian.activemq.servlet.ServletContextHolder;
import com.hevelian.activemq.support.ReversePropertySourcesStandardServletEnvironment;

public class WebXBeanBrokerFactoryTest {
	@After
	public void clearServletContextHolder() {
		if (ServletContextHolder.isServletContextSet())
			ServletContextHolder.removeServletContext();
	}

	@Test
	public void createApplicationContext_envInstanceOfReversePropertySourcesStandardServletEnvironment()
			throws MalformedURLException {
		ServletContextHolder.setServletContext(Mockito.mock(ServletContext.class));
		ApplicationContext c = new WebXBeanBrokerFactory().createApplicationContext("classpath:activemq.xml");
		assertTrue(c instanceof ResourceXmlApplicationContext);
		try (ResourceXmlApplicationContext rc = (ResourceXmlApplicationContext) c) {
			assertTrue(rc.getEnvironment() instanceof ReversePropertySourcesStandardServletEnvironment);
		}
	}
}

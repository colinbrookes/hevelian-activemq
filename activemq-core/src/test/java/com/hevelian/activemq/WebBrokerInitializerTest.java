package com.hevelian.activemq;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.activemq.broker.BrokerService;
import org.junit.Test;

public class WebBrokerInitializerTest {

	@Test
	public void contextInitialized() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(true).when(broker).waitUntilStarted();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
	}

	@Test(expected = BrokerLifecycleException.class)
	public void contextInitialized_excDuringBrokerCreate_ExcThrown() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doThrow(new BrokerLifecycleException()).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
	}

	@Test(expected = BrokerLifecycleException.class)
	public void contextInitialized_excDuringStart_ExcThrown() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doThrow(new BrokerLifecycleException()).when(broker).start();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
	}

	@Test(expected = BrokerLifecycleException.class)
	public void contextInitialized_waitUntilStartedReturnsFalse_ExcThrown() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(false).when(broker).waitUntilStarted();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
	}

	@Test
	public void contextDestroyed() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(true).when(broker).isStarted();
		doReturn(true).when(broker).waitUntilStarted();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
		i.contextDestroyed(new ServletContextEvent(sc));
	}

	@Test
	public void contextDestroyed_noBroker_doNothing() throws Exception {
		WebBrokerInitializer i = spy(WebBrokerInitializer.class);

		i.contextDestroyed(new ServletContextEvent(mock(ServletContext.class)));
	}

	@Test
	public void contextDestroyed_brokerNotStarted_doNothing() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(false).when(broker).isStarted();
		doReturn(true).when(broker).waitUntilStarted();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
		i.contextDestroyed(new ServletContextEvent(sc));

		verify(broker, never()).stop();
	}

	@Test(expected = BrokerLifecycleException.class)
	public void contextDestroyed_excDuringStop_ExcThrown() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(true).when(broker).isStarted();
		doReturn(true).when(broker).waitUntilStarted();
		doThrow(new BrokerLifecycleException()).when(broker).stop();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
		i.contextDestroyed(new ServletContextEvent(sc));
	}

	@Test(expected = BrokerLifecycleException.class)
	public void contextDestroyed_excDuringWaitUntilStopped_ExcThrown() throws Exception {
		ServletContext sc = mock(ServletContext.class);

		BrokerService broker = mock(BrokerService.class);
		doReturn(true).when(broker).isStarted();
		doReturn(true).when(broker).waitUntilStarted();
		doThrow(new BrokerLifecycleException()).when(broker).waitUntilStopped();

		WebBrokerInitializer i = spy(WebBrokerInitializer.class);
		doReturn(broker).when(i).createBroker(sc);

		i.contextInitialized(new ServletContextEvent(sc));
		i.contextDestroyed(new ServletContextEvent(sc));
	}
}

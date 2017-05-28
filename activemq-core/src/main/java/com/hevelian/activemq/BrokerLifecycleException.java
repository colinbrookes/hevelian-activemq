package com.hevelian.activemq;

/**
 * Exception is thrown during broker start/stop failures.
 * 
 * @author yflyud
 *
 */
public class BrokerLifecycleException extends RuntimeException {
	private static final long serialVersionUID = 2305080573019384106L;

	public BrokerLifecycleException() {
		super();
	}

	public BrokerLifecycleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrokerLifecycleException(String message) {
		super(message);
	}

	public BrokerLifecycleException(Throwable cause) {
		super(cause);
	}
}

package com.hevelian.activemq.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JndiLookup {
	private static final Logger LOG = LoggerFactory.getLogger(JndiLookup.class);

	public Object lookup(String name) throws NamingException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Looking up JNDI object with name '{}'", name);
		}
		Object result;
		Context ctx = new InitialContext();

		try {
			result = ctx.lookup(name);
		} catch (NamingException e) {
			try {
				ctx = (Context) ctx.lookup("java:comp/env");
				result = ctx.lookup(name);
			} catch (NamingException e1) {
				result = null;
			}
		}
		if (result == null) {
			throw new NameNotFoundException("JNDI object with name '" + name + "' not found");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T lookup(String name, Class<T> requiredType) throws NamingException {
		Object jndiObject = lookup(name);
		if (requiredType != null && !requiredType.isInstance(jndiObject)) {
			throw new NamingException("JNDI object with name '" + name + "' of type '" + jndiObject.getClass().getName()
					+ "' cannot be cast to '" + requiredType.getName() + "'");
		}
		return (T) jndiObject;
	}

}

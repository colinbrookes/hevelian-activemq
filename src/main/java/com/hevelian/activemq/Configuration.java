package com.hevelian.activemq;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Configuration {
	private Context ctx 						= null;
	private String contextHome					= null;
	private Properties properties				= new Properties();

	public Configuration(String home) {
		
		try {
		    
			ctx = new InitialContext();
			try {
				contextHome = (String) ctx.lookup(home);
			} catch(Exception e) {
				ctx = (Context) ctx.lookup("java:comp/env");
				contextHome = (String) ctx.lookup(home);
			}
			
			if(contextHome==null) {
				System.out.println("HevelianActiveMQ: JNDI "+home+" not correctly configured in context.xml.");
				return;
			}
			
			String confFile = contextHome + "/conf/hevelian.properties";
			properties.load(new FileReader(new File(confFile)));
			
		} catch (Exception e) {
			System.out.println("HevelianActiveMQ: JNDI "+home+" exception: " + e.getMessage());
		}
	}
	
	/**
	 * Sets a given property to a given value. Allows overrides on defined properties or adding new properties
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, String value) {
		properties.put(name, value);
	}
	
	/**
	 * Returns the value of a given property
	 * @param _name
	 * @return
	 */
	public String getProperty(String _name) {
		return (String) properties.get(_name);
	}

}

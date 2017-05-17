package com.hevelian.activemq.support;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * By default the JNDI and System properties have the last priority. We need to
 * give users the possibility to overwrite the default configuration using one
 * of the above mentioned property sources.
 * 
 * @author yflyud
 *
 */
public class ReversePropertySourcesStandardServletEnvironment extends StandardServletEnvironment {
	@Override
	protected void customizePropertySources(MutablePropertySources propertySources) {
		super.customizePropertySources(propertySources);
		propertySources.forEach(ps -> propertySources.addFirst(ps));
	}
}

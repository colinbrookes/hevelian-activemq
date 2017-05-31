package com.hevelian.activemq.support;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

public class ReversePropertySourcesStandardServletEnvironmentTest {

	@Test
	public void customizePropertySources() {
		MutablePropertySources propertySources = new MutablePropertySources();
		ReversePropertySourcesStandardServletEnvironment env = new ReversePropertySourcesStandardServletEnvironment();
		env.customizePropertySources(propertySources);
		
		String[] sourceNames = { StandardServletEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
				StandardServletEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,
				StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME,
				StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME };
		List<String> result = new ArrayList<>();
		propertySources.forEach(a -> result.add(a.getName()));
		Assert.assertArrayEquals(sourceNames, result.toArray());
	}

}

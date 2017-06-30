package com.sino.scaffold.config;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Function;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * @author kerbores
 *
 */
@Configuration
public class BeetlConfig {

	@Autowired
	ApplicationContext context;

	@Bean(initMethod = "init")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {

		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
		ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
		try {
			String root = patternResolver.getResource("classpath:templates").getFile().toString();
			WebAppResourceLoader webAppResourceLoader = new WebAppResourceLoader(root);
			beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
			beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource("classpath:beetl.properties"));
			Map<String, Function> functions = context.getBeansOfType(Function.class);
			beetlGroupUtilConfiguration.setFunctions(functions);
			return beetlGroupUtilConfiguration;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Bean(name = "beetlViewResolver")
	@ConditionalOnBean(BeetlGroupUtilConfiguration.class)
	public BeetlSpringViewResolver getBeetlSpringViewResolver(BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}

}

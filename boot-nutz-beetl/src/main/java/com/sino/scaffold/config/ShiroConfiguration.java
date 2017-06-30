package com.sino.scaffold.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.sino.scaffold.ext.shiro.SINOAdvisor;
import com.sino.scaffold.ext.shiro.cache.RedisCacheManager;
import com.sino.scaffold.ext.shiro.matcher.SINOCredentialsMatcher;
import com.sino.scaffold.ext.shiro.realm.SINORealm;

@Configuration
public class ShiroConfiguration {

	private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

	@Bean
	public CredentialsMatcher credentialsMatcher() {
		return new SINOCredentialsMatcher();
	}

	@Bean
	public CacheManager getCache(RedisConnectionFactory connectionFactory) {
		return new RedisCacheManager(connectionFactory);
	}

	@Bean
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@ConditionalOnBean({ CacheManager.class, SINORealm.class, CredentialsMatcher.class })
	public DefaultWebSecurityManager getDefaultWebSecurityManager(CacheManager cacheManager, SINORealm afdiRealm, CredentialsMatcher credentialsMatcher) {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
		afdiRealm.setCredentialsMatcher(credentialsMatcher);
		dwsm.setRealm(afdiRealm);
		dwsm.setCacheManager(cacheManager);
		return dwsm;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	@ConditionalOnBean(SecurityManager.class)
	public Advisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		SINOAdvisor aasa = new SINOAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	@Bean(name = "shiroFilter")
	@ConditionalOnBean(SecurityManager.class)
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean
				.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/login");
		// shiroFilterFactoryBean.setSuccessUrl("/sa/index");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		// filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
}

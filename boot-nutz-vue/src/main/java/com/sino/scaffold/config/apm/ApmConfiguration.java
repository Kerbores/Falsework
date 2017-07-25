package com.sino.scaffold.config.apm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Dao;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import club.zhcs.apm.APMAppender;
import club.zhcs.apm.UserCollector;

/**
 * @author kerbores
 *
 */
@Configuration
public class ApmConfiguration {

	@Bean
	public APMAppender apmAppender(Dao dao) {
		return new APMAppender() {
			Log logger = Logs.get();

			@Override
			public void append(APMLog log) {
				logger.debugf("apm--> %s", log);
			}
		};
	}

	@Bean
	public UserCollector userCollector() {
		return new UserCollector() {

			@Override
			public String collector() {
				Subject subject = SecurityUtils.getSubject();
				if (subject == null) {
					return null;
				}
				Object principal = subject.getPrincipal();
				if (principal == null) {
					return null;
				}
				return principal.toString();
			}
		};
	}

}

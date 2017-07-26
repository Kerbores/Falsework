package com.sino.scaffold.config.apm;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Times;
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

	@Table("t_apm_log_${month}")
	public static class LogDO {
		/**
		 * 
		 */
		public LogDO() {
		}

		@Id
		long id;

		@Column("l_tag")
		String tag;

		@Column("l_user")
		String user;

		@Column("l_action_time")
		Date actionTime;

		@Column("l_action_duration")
		long actionDuration;

		@Column("l_args")
		@ColDefine(type = ColType.TEXT)
		String args;

		@Column("l_return")
		@ColDefine(type = ColType.TEXT)
		String retuenObj;

		@Column("l_exception")
		boolean exception;

	}

	@Bean
	public APMAppender apmAppender(Dao dao) {
		return new APMAppender() {

			Log logger = Logs.get();

			@Override
			public void append(APMLog log) {
				TableName.set(Times.format("yyyyMM", Times.now()));
				dao.create(LogDO.class, false);// 保证表妥妥的在
				LogDO l = new LogDO();
				l.actionDuration = log.getActionDuration();
				l.actionTime = log.getActionTime();
				l.args = Json.toJson(log.getArgs(), JsonFormat.compact());
				l.exception = log.isException();
				l.retuenObj = Json.toJson(log.getRetuenObj(), JsonFormat.compact());
				l.tag = log.getTag();
				l.user = log.getUser();
				dao.insert(l);
				logger.debugf("apm--> %s", Json.toJson(log));
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

package com.sino.scaffold.aop.apm;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nutz.castor.Castors;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sino.scaffold.bean.log.OperationLog;
import com.sino.scaffold.utils.Result;

/**
 * @author kerbores kerbores@gmail.com
 *
 */
@Aspect
@Component
public class APMInterceptor {

	@Autowired
	private Dao dao;

	@Autowired
	HttpServletRequest request;

	private Log LOG = Logs.getLog(getClass());

	@Pointcut("@annotation(com.sino.scaffold.aop.apm.SystemLog)")
	public void cut() {
	}

	public SystemLog getSystemLog(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		SystemLog target = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					target = method.getAnnotation(SystemLog.class);
					break;
				}
			}
		}
		return target;
	}

	@Around("cut()")
	public Object filter(ProceedingJoinPoint point) throws Throwable {
		SystemLog log = getSystemLog(point);
		String ip = Lang.getIP(request);
		String user = SecurityUtils.getSubject().getPrincipal() == null ? null : SecurityUtils.getSubject().getPrincipal().toString();
		OperationLog operationLog = new OperationLog();
		operationLog.setAccount(user);
		operationLog.setAction(log.method());
		operationLog.setIp(ip);
		operationLog.setModule(log.module());

		Stopwatch stopwatch = Stopwatch.begin();
		Object obj = point.proceed();
		stopwatch.stop();
		if (obj instanceof Result) {
			operationLog.setDescription(Castors.me().castTo(obj, Result.class).isSuccess() ? "操作成功" : "操作失败");
		}
		if (Strings.isBlank(operationLog.getDescription())) {
			operationLog.setDescription(log.description());
		}

		operationLog.setOperationTime(stopwatch.getDuration());
		save(operationLog);
		return obj;
	}

	public void save(OperationLog log) throws InterruptedException {
		if (dao == null) {
			LOG.debug(log);
		} else {
			dao.insert(log);
		}
	}

}

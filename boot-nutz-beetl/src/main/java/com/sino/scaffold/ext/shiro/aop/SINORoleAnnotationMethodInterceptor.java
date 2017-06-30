package com.sino.scaffold.ext.shiro.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.RoleAnnotationMethodInterceptor;

/**
 * 
 * @author kerbores
 *
 * @email kerbores@gmail.com
 *
 */
public class SINORoleAnnotationMethodInterceptor extends RoleAnnotationMethodInterceptor {

	public SINORoleAnnotationMethodInterceptor() {
		setHandler(new SINORoleAnnotationHandler());
	}

	public SINORoleAnnotationMethodInterceptor(AnnotationResolver resolver) {
		setHandler(new SINORoleAnnotationHandler());
		setResolver(resolver);
	}
}

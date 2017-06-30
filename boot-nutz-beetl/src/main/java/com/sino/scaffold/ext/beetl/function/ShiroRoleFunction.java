package com.sino.scaffold.ext.beetl.function;

import org.apache.shiro.SecurityUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.stereotype.Service;

/**
 * 
 * @author kerbores
 *
 * @email kerbores@gmail.com
 *
 */
@Service("hasRole")
public class ShiroRoleFunction implements Function {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Function#call(java.lang.Object[], org.beetl.core.Context)
	 */
	@Override
	public Object call(Object[] role, Context context) {
		if (role == null || role.length < 1) {
			return false;
		}
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().hasRole(role[0].toString());
	}

}

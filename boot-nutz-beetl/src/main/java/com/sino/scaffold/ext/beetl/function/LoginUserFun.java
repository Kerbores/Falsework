package com.sino.scaffold.ext.beetl.function;

import org.apache.shiro.SecurityUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.service.acl.UserService;

/**
 * 
 * @author kerbores
 *
 * @email kerbores@gmail.com
 *
 */
@Service("loginUser")
public class LoginUserFun implements Function {

	@Autowired
	UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Function#call(java.lang.Object[], org.beetl.core.Context)
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		Object principal = SecurityUtils.getSubject().getPrincipal();
		String userName = principal == null ? null : principal.toString();
		if (Strings.isBlank(userName)) {
			return null;
		}
		User user = userService.fetch(userName);
		if (paras != null && paras.length > 0) {
			String key = paras[0].toString();
			return user == null ? null : Lang.obj2nutmap(user).get(key);
		}
		return user;
	}

}

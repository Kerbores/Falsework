package com.sino.scaffold.ext.beetl.function;

import javax.servlet.http.HttpServletRequest;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sino.scaffold.config.ConfigLoader;

/***
 * 
 * @author kerbores
 *
 */
@Service("config")
public class ConfigFun implements Function {

	@Autowired
	ConfigLoader configLoader;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.beetl.core.Function#call(java.lang.Object[], org.beetl.core.Context)
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		if (paras == null) {
			return null;
		}
		switch (paras[0].toString()) {
		case "debug":
			HttpServletRequest request = (HttpServletRequest) ctx.getGlobal("request");
			return Strings.equalsIgnoreCase("localhost", request.getServerName())
					|| Strings.equalsIgnoreCase("127.0.0.1", request.getServerName());
		default:
			return configLoader.get(paras[0].toString());
		}
	}

}

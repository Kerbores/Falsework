package com.sino.scaffold.hanlder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthenticatedException;
import org.nutz.lang.Strings;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sino.scaffold.utils.OperationState;
import com.sino.scaffold.utils.Result;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
	Logger logger = Logger.getLogger(getClass());

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
		logger.error("error=>", e);
		ModelAndView m = new ModelAndView("jsonView");
		if (e instanceof UnauthenticatedException) {
			if (!Strings.equalsIgnoreCase("XMLHttpRequest", req.getHeader("X-Requested-With"))) {
				return new ModelAndView("redirect:/");
			}
			response.setStatus(401);
			m.addObject(Result.me()
					.setOperationState(OperationState.UNLOGINED)
					.addData("msg", "没有权限"));
			return m;
		}
		m.addObject(Result.exception(e));
		return m;
	}
}

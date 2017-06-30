package com.sino.scaffold.hanlder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.utils.OperationState;
import com.sino.scaffold.utils.Result;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
	Logger logger = Logger.getLogger(getClass());

	@ExceptionHandler(value = Exception.class)
	public Result defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
		logger.error("error=>", e);
		if (e instanceof UnauthenticatedException) {
			response.setStatus(401);
			return Result.me()
					.setOperationState(OperationState.UNLOGINED)
					.addData("msg", "没有权限");
		}
		return Result.exception(e);
	}
}

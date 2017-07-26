package com.sino.scaffold.controller;

import java.io.IOException;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import club.zhcs.apm.APM;
import club.zhcs.captcha.CaptchaView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@Controller
@Api(value = "Index", tags = { "通用" })
public class IndexController {
	Log logger = Logs.get();

	@GetMapping("/captcha")
	@ApiOperation("验证码")
	@APM("验证码")
	public CaptchaView captcha(@RequestParam(value = "length", required = false, defaultValue = "4") @ApiParam(value = "验证码长度", required = false, defaultValue = "4") int length)
			throws IOException {
		return new CaptchaView(length);
	}

}

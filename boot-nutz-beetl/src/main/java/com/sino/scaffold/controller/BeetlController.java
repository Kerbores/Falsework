package com.sino.scaffold.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sino.scaffold.captcha.ImageVerification;
import com.sino.scaffold.service.acl.UserService;

/**
 * @author kerbores
 *
 */
@Controller
public class BeetlController {
	Log logger = Logs.get();

	@Autowired
	HttpServletRequest request;

	@Autowired
	UserService userService;

	public static final String CAPTCHA_KEY = "SINO_CAPTCHA";

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("test", "Hello Beetl");
		model.addAttribute("title", "用户登录");
		return "pages/login/login.html";
	}

	@GetMapping("/captcha")
	public void captcha(@RequestParam(value = "length", required = false, defaultValue = "4") int length,
			HttpServletResponse resp, HttpSession session) throws IOException {
		resp.setContentType("image/jpeg");
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		OutputStream out = resp.getOutputStream();
		// 输出图象到页面
		ImageVerification iv = new ImageVerification();

		if (length != 0) {
			iv.setIMAGE_VERIFICATION_LENGTH(length);
		}

		if (ImageIO.write(iv.creatImage(), "JPEG", out)) {
			logger.debug("写入输出流成功:" + iv.getVerifyCode() + ".");
		} else {
			logger.debug("写入输出流失败:" + iv.getVerifyCode() + ".");
		}

		session.setAttribute(CAPTCHA_KEY, iv.getVerifyCode());

		// 以下关闭输入流！
		out.flush();
		out.close();
	}

	@GetMapping("/layout")
	public String layout(Model model) {
		model.addAttribute("obj", NutMap.NEW().addv("i", 1).addv("d", Times.now()));
		model.addAttribute("users", userService.queryAll());
		return "pages/test.html";
	}

}

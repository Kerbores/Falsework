package com.sino.scaffold.controller.acl;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sino.scaffold.BootNutzBeetlApplication;
import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresRoles;
import com.sino.scaffold.service.acl.ShiroUserService;
import com.sino.scaffold.utils.DES;
import com.sino.scaffold.utils.Result;

/**
 * @author kerbores
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

	@Autowired
	ShiroUserService shiroUserService;

	@PostMapping("login")
	@ResponseBody
	public Result login(@RequestParam("user") String user, @RequestParam("password") String password, @RequestParam("captcha") String captcha,
			@RequestParam("rememberMe") boolean rememberMe, HttpSession session) {
		if (Strings.equalsIgnoreCase(captcha, session.getAttribute(BootNutzBeetlApplication.CAPTCHA_KEY).toString())) {
			Result result = shiroUserService.login(user, password);
			if (result.isSuccess()) {
				// 登录成功处理
				_putSession(BootNutzBeetlApplication.USER_KEY, result.getData().get("loginUser"));
				if (rememberMe) {
					NutMap data = NutMap.NEW();
					data.put("user", user);
					data.put("password", password);
					data.put("rememberMe", rememberMe);
					_addCookie(BootNutzBeetlApplication.USER_COOKIE_KEY, DES.encrypt(Json.toJson(data)), 24 * 60 * 60 * 365);
				}
			}
			return result;
		} else {
			return Result.fail("验证码输入错误");
		}
	}

	@GetMapping("logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/";
	}

	@GetMapping("shiro")
	@SINORequiresRoles(InstalledRole.SU)
	public Result shiro() {
		return Result.success();
	}

}

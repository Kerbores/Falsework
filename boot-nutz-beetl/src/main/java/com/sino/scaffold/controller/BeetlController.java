package com.sino.scaffold.controller;

import javax.servlet.http.HttpServletRequest;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sino.scaffold.BootNutzBeetlApplication;
import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresRoles;
import com.sino.scaffold.service.acl.UserService;
import com.sino.scaffold.utils.DES;

/**
 * @author kerbores
 *
 */
@Controller
public class BeetlController extends BaseController {
	Log logger = Logs.get();

	@Autowired
	HttpServletRequest request;

	@Autowired
	UserService userService;

	@GetMapping("/")
	@RequestMapping("/")
	public String home(Model model, @SessionAttribute(name = BootNutzBeetlApplication.USER_KEY, required = false) User user) {
		if (user != null) {
			return "redirect:/dashboard";
		}
		String cookie = _getCookie(BootNutzBeetlApplication.USER_COOKIE_KEY);
		NutMap data = NutMap.NEW();
		if (!Strings.isBlank(cookie)) {
			data = Lang.map(DES.decrypt(cookie));
		}
		model.addAttribute("loginInfo", data);
		model.addAttribute("title", "系统登录");
		return "pages/login/login.html";
	}

	@GetMapping("dashboard")
	@SINORequiresRoles(InstalledRole.SU)
	public String dashboard() {
		return "pages/dashboard.html";
	}

	@GetMapping("/layout")
	public String layout(Model model) {
		model.addAttribute("obj", NutMap.NEW().addv("i", 1).addv("d", Times.now()));
		model.addAttribute("users", userService.queryAll());
		return "pages/test.html";
	}

}

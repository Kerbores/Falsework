package com.sino.scaffold.controller.acl;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Ok;
import org.nutz.plugin.spring.boot.service.entity.PageredData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sino.scaffold.BootNutzVueApplication;
import com.sino.scaffold.bean.InstallPermission;
import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresPermissions;
import com.sino.scaffold.ext.shiro.anno.SINORequiresRoles;
import com.sino.scaffold.service.acl.ShiroUserService;
import com.sino.scaffold.service.acl.UserService;
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

	@Autowired
	UserService userService;

	/**
	 * 用户列表
	 * 
	 * @param page
	 *            页码
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresPermissions(InstallPermission.USER_LIST)
	public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		page = _fixPage(page);
		PageredData<User> pager = userService.searchByPage(page, Cnd.NEW().desc("id"));
		model.addAttribute("obj", Result.success().addData("pager", pager).setTitle("用户列表"));
		return "pages/admin/auth/user/list.html";
	}

	/**
	 * 添加用户页面
	 * 
	 * @return
	 */
	@GetMapping(value = "add")
	@SINORequiresPermissions(InstallPermission.USER_ADD)
	public String add(Model model) {
		model.addAttribute("obj", Result.success().setTitle("添加用户"));
		return "pages/admin/auth/user/add_edit.html";
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            待添加用户
	 * @return
	 */
	@PostMapping(value = "add")
	@SINORequiresPermissions(InstallPermission.USER_ADD)
	public @ResponseBody Result add(User user) {
		user.setPassword(Lang.md5(user.getPassword()));
		return userService.save(user) != null ? Result.success().addData("user", user) : Result.fail("添加用户失败!");
	}

	/**
	 * 编辑用户页面
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping(value = "/edit/{id}")
	@Ok("beetl:pages/admin/auth/user/add_edit.html")
	@SINORequiresPermissions(InstallPermission.USER_EDIT)
	public String edit(@PathVariable("id") int id, Model model) {
		model.addAttribute("obj", Result.success().addData("user", userService.fetch(id)).setTitle("编辑用户"));
		return "pages/admin/auth/user/add_edit.html";
	}

	/**
	 * 编辑用户
	 * 
	 * @param user
	 *            待更新用户
	 * @return
	 */
	@PostMapping(value = "edit")
	@SINORequiresPermissions(InstallPermission.USER_EDIT)
	public @ResponseBody Result edit(User user) {
		return userService.update(user, "realName", "phone", "email", "status") ? Result.success() : Result.fail("更新失败!");
	}

	/**
	 * 搜索用户
	 * 
	 * @param key
	 *            关键词
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresPermissions(InstallPermission.USER_LIST)
	public String search(@RequestParam("key") String key, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		page = _fixPage(page);
		key = _fixSearchKey(key);
		PageredData<User> pager = userService.searchByKeyAndPage(key, page, "name", "nickName", "realName");
		model.addAttribute("obj", Result.success().addData("pager", pager).addData("searchKeys", NutMap.NEW().addv("key", key)).setTitle("用户检索"));
		return "pages/admin/auth/user/list.html";
	}

	/**
	 * 用户详情
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping("/detail/{id}")
	@SINORequiresPermissions(InstallPermission.USER_DETAIL)
	public String detail(@PathVariable("id") int id, Model model) {
		model.addAttribute("obj", Result.success().addData("user", userService.fetch(id)).setTitle("用户详情"));
		return "pages/admin/auth/user/detail.html";
	}

	@PostMapping("login")
	@ResponseBody
	public Result login(@RequestParam("user") String user, @RequestParam("password") String password, @RequestParam("captcha") String captcha,
			@RequestParam("rememberMe") boolean rememberMe, HttpSession session) {
		if (Strings.equalsIgnoreCase(captcha, session.getAttribute(BootNutzVueApplication.CAPTCHA_KEY).toString())) {
			Result result = shiroUserService.login(user, password);
			if (result.isSuccess()) {
				// 登录成功处理
				_putSession(BootNutzVueApplication.USER_KEY, result.getData().get("loginUser"));
				if (rememberMe) {
					NutMap data = NutMap.NEW();
					data.put("user", user);
					data.put("password", password);
					data.put("rememberMe", rememberMe);
					_addCookie(BootNutzVueApplication.USER_COOKIE_KEY, DES.encrypt(Json.toJson(data)), 24 * 60 * 60 * 365);
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

package com.sino.scaffold.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.plugin.spring.boot.service.entity.PageredData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.BootNutzBeetlApplication;
import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.bean.acl.User.Status;
import com.sino.scaffold.captcha.ImageVerification;
import com.sino.scaffold.service.acl.UserService;

/**
 * @author kerbores
 *
 */
@RestController
public class IndexController {
	Log logger = Logs.get();

	@Autowired
	UserService userService;

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
		session.setAttribute(BootNutzBeetlApplication.CAPTCHA_KEY, iv.getVerifyCode());
		// 以下关闭输入流！
		out.flush();
		out.close();
	}

	@GetMapping("list")
	public List<User> list() {
		return userService.queryAll();
	}

	@GetMapping("add")
	public User add() {
		User user = new User();
		user.setStatus(R.random(0, 2) == 1 ? Status.ACTIVED : Status.DISABLED);
		user.setName(String.format("sino%d", R.random(0, 100000)));
		user.setEmail(String.format("test%d@sinosoft.com.cn", R.random(0, 100000)));
		userService.save(user);
		return user;
	}

	@GetMapping("delete/{id}")
	public int delete(@PathVariable("id") long id) {
		return userService.delete(id);
	}

	@GetMapping("page/{page}")
	public PageredData<User> pagerAll(@PathVariable("page") int page) {
		return userService.searchByPage(page, 10, null);
	}

	@GetMapping("search/{key}/{page}")
	public PageredData<User> pagerSearch(@PathVariable("key") String key, @PathVariable("page") int page) {
		return userService.searchByPage(page, 10, Cnd.where("userName", "like", String.format("%%%s%%", key)));
	}
}

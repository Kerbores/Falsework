package com.sino.scaffold.controller;

import javax.servlet.http.HttpServletRequest;

import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sino.scaffold.service.UserService;

/**
 * @author kerbores
 *
 */
@Controller
@RequestMapping("beetl")
public class BeetlController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("test", "Hello Beetl");
		model.addAttribute("users", userService.queryAll());
		return "pages/login/login.html";
	}
	
	@GetMapping("/layout")
	public String layout(Model model) {
		model.addAttribute("obj", NutMap.NEW().addv("i", 1).addv("d", Times.now()));
		model.addAttribute("users", userService.queryAll());
		return "pages/test.html";
	}

}

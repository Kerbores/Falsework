package com.sino.scaffold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sino.scaffold.biz.UserService;

/**
 * @author kerbores
 *
 */
@Controller
@RequestMapping("beetl")
public class BeetlController {
	
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("test", "Hello Beetl");
		model.addAttribute("users", userService.listAll());
		return "index.btl";
	}

}

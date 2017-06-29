package com.sino.scaffold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.plugins.Page;
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
		model.addAttribute("users", userService.selectPage(new Page<>(0, Integer.MAX_VALUE)).getRecords());
		return "index.btl";
	}

}

package com.sino.scaffold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.biz.UserService;
import com.sino.scaffold.model.User;

/**
 * @author kerbores
 *
 */
@RestController
public class IndexController {
	
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String index() {
		return "Hello sino soft!";
	}
	
	@GetMapping("list")
	public List<User> list() {
		return userService.listAll();
	}

}

package com.sino.scaffold.controller.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.Result;
import com.sino.scaffold.service.acl.ShiroUserService;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	ShiroUserService shiroUserService;

	@GetMapping("login")
	public Result login(@RequestParam String userName, @RequestParam String password) {
		return shiroUserService.login(userName, password);
	}

}

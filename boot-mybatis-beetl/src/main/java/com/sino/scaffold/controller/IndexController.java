package com.sino.scaffold.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kerbores
 *
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public String indx() {
		return "Hello sino soft!";
	}

}

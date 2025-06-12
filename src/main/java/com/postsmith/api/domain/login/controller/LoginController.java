package com.postsmith.api.domain.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/uu")
	public String login() {
		return "Login endpoint is active";
	}
}

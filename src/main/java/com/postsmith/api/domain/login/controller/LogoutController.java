package com.postsmith.api.domain.login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postsmith.api.domain.login.service.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {
	private final RedisService redisService;

	@PostMapping
	public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseObject = new HashMap<String, Object>();

		String sessionId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			responseObject.put("message", "No cookies found");
			return new ResponseEntity<Object>(responseObject, HttpStatus.BAD_REQUEST);
		}
		for (Cookie cookie : cookies) {
			if ("CLIENT_SESSION_ID".equals(cookie.getName())) {
				sessionId = cookie.getValue();
				break;
			}
		}

		Cookie cookieSession = new Cookie("CLIENT_SESSION_ID", null);
		cookieSession.setMaxAge(0);
		cookieSession.setPath("/");
		response.addCookie(cookieSession);

		if (sessionId != null) {
			redisService.deleteValue(sessionId);
		}

		responseObject.put("message", "Logout successful");
		responseObject.put("success", true);
		return new ResponseEntity<Object>(responseObject, HttpStatus.OK);
	}
}

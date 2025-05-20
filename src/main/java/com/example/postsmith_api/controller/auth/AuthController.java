package com.example.postsmith_api.controller.auth;

import com.example.postsmith_api.config.auth.dto.OAuth2Response;
import com.example.postsmith_api.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/success")
    public ResponseEntity<?> login(@RequestBody OAuth2Response response,
                                   HttpServletResponse httpServletResponse) {
        Cookie cookie = authService.login(response.getEmail());
        httpServletResponse.addCookie(cookie);
        System.out.println("cookie = " + cookie);
        return ResponseEntity.ok("로그인 성공");
    }
}

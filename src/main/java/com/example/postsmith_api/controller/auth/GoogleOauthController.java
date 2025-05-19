package com.example.postsmith_api.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleOauthController {
    @GetMapping("/login")
    public String googleLogin() {
        return "login";
    }
}

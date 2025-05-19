package com.example.postsmith_api.controller;

import lombok.Getter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class OAuthPracticeController {
    @GetMapping("/home")
    public String home() {
        return "로그인 성공";
    }
}

package com.example.postsmith_api.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthPracticeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}

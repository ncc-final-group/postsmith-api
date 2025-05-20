package com.example.postsmith_api.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    @GetMapping("/api1/test")
    public String test(HttpServletRequest request, Model model){
        String email = request.getHeader("email");
        System.out.println("email = " + email);
        model.addAttribute("email", email);
        return "home";
    }
}

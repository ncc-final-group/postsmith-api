package com.example.postsmith_api.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api1/posts")
public class PostsController {
    @PostMapping("/create")
    public ResponseEntity<?> createPost() {

        return ResponseEntity.ok("Post created successfully");
    }
}

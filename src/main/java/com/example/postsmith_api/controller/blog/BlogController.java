package com.example.postsmith_api.controller.blog;

import com.example.postsmith_api.dto.BlogDto;
import com.example.postsmith_api.dto.CategoryCreateDto;
import com.example.postsmith_api.service.blog.BlogService;
import com.example.postsmith_api.service.manage.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api1/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createBlog(@RequestBody BlogDto blogDto) {
        return ResponseEntity.ok(blogService.createBlog(blogDto));
    }
    @PostMapping("/category/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreateDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }
}

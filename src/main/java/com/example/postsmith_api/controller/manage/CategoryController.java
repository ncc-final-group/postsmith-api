package com.example.postsmith_api.controller.manage;

import com.example.postsmith_api.dto.CategoryDto;
import com.example.postsmith_api.service.manage.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/api1/category/create")
    public ResponseEntity<?> createCategory(CategoryDto categoryDto, HttpRequest request){
        return ResponseEntity.ok(categoryService.createCategory(categoryDto, request));
    }
}

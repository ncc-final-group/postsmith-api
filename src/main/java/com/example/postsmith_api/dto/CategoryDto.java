package com.example.postsmith_api.dto;

import com.example.postsmith_api.domain.blog.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryDto {
    private int blogId;
    private List<Category> categoryList;
}

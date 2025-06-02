package com.example.postsmith_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateDto {
    private int blogId;
    private String categoryName;
    private int parentCategoryId;
}

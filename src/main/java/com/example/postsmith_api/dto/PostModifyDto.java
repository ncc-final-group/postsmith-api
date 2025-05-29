package com.example.postsmith_api.dto;

import lombok.Getter;

@Getter
public class PostModifyDto {
    private String category;
    private String title;
    private String contentHtml;
}

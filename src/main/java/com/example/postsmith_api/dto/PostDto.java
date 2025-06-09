package com.example.postsmith_api.dto;

import lombok.Getter;

@Getter
public class PostDto {
    private int blogId;
    private int postId;
    private int category;
    private String title;
    private String content;
    private String postType = "posts"; // Default post type
    @Override
    public String toString() {
        return "PostDto{" +
                "blogId=" + blogId +
                ", postId=" + postId +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postType='" + postType + '\'' +
                '}';
    }
}

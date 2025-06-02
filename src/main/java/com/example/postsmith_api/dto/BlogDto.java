package com.example.postsmith_api.dto;

import com.example.postsmith_api.domain.blog.Blog;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogDto {
    private int blogId;
    private int userId;
    private String blogName;
    private String blogUrl;
    private String blogNickname;
    public Blog toEntity(){
        return Blog.builder()
                .userId(userId)
                .blogName(blogName)
                .blogUrl(blogUrl)
                .blogNickname(blogNickname)
                .build();
    }
    @Override
    public String toString() {
        return "BlogDto{" +
                "blogId=" + blogId +
                ", userId=" + userId +
                ", blogName='" + blogName + '\'' +
                ", blogUrl='" + blogUrl + '\'' +
                ", blogNickname='" + blogNickname + '\'' +
                '}';
    }
}

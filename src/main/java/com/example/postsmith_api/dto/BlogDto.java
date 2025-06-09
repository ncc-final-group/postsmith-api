package com.example.postsmith_api.dto;

import com.example.postsmith_api.domain.blog.Blogs;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogDto {
    private int blogId;
    private int userId;
    private String blogName;
    private String address;
    private String blogNickname;
    private String postType;
    public Blogs toEntity(){
        return Blogs.builder()
                .userId(userId)
                .blogName(blogName)
                .address(address)
                .blogNickname(blogNickname)
                .build();
    }
    @Override
    public String toString() {
        return "BlogDto{" +
                "blogId=" + blogId +
                ", userId=" + userId +
                ", blogName='" + blogName + '\'' +
                ", address='" + address + '\'' +
                ", blogNickname='" + blogNickname + '\'' +
                '}';
    }
}

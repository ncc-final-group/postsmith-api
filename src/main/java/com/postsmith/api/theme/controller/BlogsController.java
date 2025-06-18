package com.postsmith.api.theme.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postsmith.api.theme.dto.BlogsDto;
import com.postsmith.api.theme.service.BlogsService;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogsController {

    private final BlogsService blogsService;

    // 추천 구독 블로그
    @GetMapping("/recommendedBlogs/userId/{userId}")
    public List<BlogsDto> findrecommendedBlogs(@PathVariable("userId") Integer userId) {
        return blogsService.findrecommendedBlogs(userId);
    }

    // 구독
    @PutMapping("/subscription/subscriberId/{subscriberId}/blogId/{blogId}")
    public ResponseEntity<Void> subscribeBlog(@PathVariable("subscriberId") Integer subscriberId, @PathVariable("blogId") Integer blogId) {
    	blogsService.subscribeBlog(subscriberId, blogId);
        return ResponseEntity.ok().build();
    }
}

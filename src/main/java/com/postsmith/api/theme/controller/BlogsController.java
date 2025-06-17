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

    // userId로 운영 중인 블로그 정보
    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<BlogsDto>> getBlogsByUserId(@PathVariable("userId") Integer userId) {
        List<BlogsDto> blogList = blogsService.getBlogsByUserId(userId);
        return ResponseEntity.ok(blogList);
    }

    // blog_id로 블로그 정보
    @GetMapping("/{id}")
    public ResponseEntity<BlogsDto> getBlogById(@PathVariable("id") Integer id) {
        return blogsService.getBlogById(id);
    }
    
    // 블로그 정보 업데이트 
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateBlog(@PathVariable("id") Integer id, @RequestBody BlogsDto dto) {
    	blogsService.updateBlog(id, dto);
        return ResponseEntity.ok().build();
    }

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

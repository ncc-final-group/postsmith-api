package com.example.postsmith_api.controller.manage;

import com.example.postsmith_api.domain.blog.Blog;
import com.example.postsmith_api.domain.blog.Category;
import com.example.postsmith_api.dto.PostDto;
import com.example.postsmith_api.service.blog.BlogService;
import com.example.postsmith_api.service.manage.CategoryService;
import com.example.postsmith_api.service.manage.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api1/post")
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;
    private final CategoryService categoryService;
    private final BlogService blogService;
    @PostMapping("/{url}/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto, @PathVariable("url") String url) {
        Blog blog = blogService.findBlogByBlogUrl(url);
        // Check if the blog exists
        if(blog == null) {
            return ResponseEntity.badRequest().body("Blog not found with the given URL");
        }
        postsService.createPost(blog, dto);
        return ResponseEntity.ok("Post created successfully");
    }
}

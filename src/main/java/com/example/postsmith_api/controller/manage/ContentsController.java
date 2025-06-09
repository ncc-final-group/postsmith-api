package com.example.postsmith_api.controller.manage;

import com.example.postsmith_api.domain.blog.Blogs;
import com.example.postsmith_api.dto.PostDto;
import com.example.postsmith_api.service.blog.BlogService;
import com.example.postsmith_api.service.manage.CategoryService;
import com.example.postsmith_api.service.manage.ContentsService;
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
public class ContentsController {
    private final ContentsService contentsService;
    private final CategoryService categoryService;
    private final BlogService blogService;
    @PostMapping("/{url}/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto, @PathVariable("url") String url) {
        Blogs blog = blogService.findBlogByAddress(url);
        // Check if the blog exists
        if(blog == null) {
            return ResponseEntity.badRequest().body("Blog not found with the given URL");
        }
        contentsService.createPost(blog, dto);
        return ResponseEntity.ok(dto);
    }
}

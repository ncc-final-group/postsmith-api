package com.example.postsmith_api.service.blog;

import com.example.postsmith_api.domain.blog.Blog;
import com.example.postsmith_api.dto.BlogDto;
import com.example.postsmith_api.repository.manage.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    public String createBlog(BlogDto blogDto){
        System.out.println(blogDto);
        if(blogRepository.existsByBlogUrl(blogDto.getBlogUrl())){
            return "Blog URL already exists";
        }
        blogRepository.save(
                blogDto.toEntity()
        );
        return "Blog created successfully";
    }
    public Blog findBlogByBlogUrl(String blogUrl) {
        return blogRepository.findByBlogUrl(blogUrl)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with URL: " + blogUrl));    }
}

package com.example.postsmith_api.service.blog;

import com.example.postsmith_api.domain.blog.Blogs;
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
        if(blogRepository.existsByAddress(blogDto.getAddress())){
            return "Blog URL already exists";
        }
        blogRepository.save(
                blogDto.toEntity()
        );
        return "Blog created successfully";
    }
    public Blogs findBlogByAddress(String url) {
            return blogRepository.findByAddress(url)
                    .orElseThrow(() -> new IllegalArgumentException("Blog not found with URL: " + url));
    }
}

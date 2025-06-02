package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Blog;
import com.example.postsmith_api.domain.blog.Category;
import com.example.postsmith_api.domain.blog.Posts;
import com.example.postsmith_api.dto.PostDto;
import com.example.postsmith_api.repository.manage.CategoryRepository;
import com.example.postsmith_api.repository.manage.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postRepository;
    private final CategoryRepository categoryRepository;
    public void createPost(Blog blog, PostDto dto) {
        Category category = categoryRepository.findById(dto.getCategory())
                .orElse(null);
        String cleanContent = dto.getContent().replaceAll("<[^>]*>", "");
        System.out.println(cleanContent);

        postRepository.save(
                Posts.builder()
                        .blogId(blog)
                        .categoryId(category)
                        .title(dto.getTitle())
                        .contentHtml(dto.getContent())
                        .contentClean(cleanContent)
                        .build()
        );
    }
}

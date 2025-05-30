package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Posts;
import com.example.postsmith_api.repository.manage.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postRepository;

    public void createPost(String category, String title, String contentHtml) {
        postRepository.save(
                Posts.builder()
                        .category(category)
                        .title(title)
                        .contentHtml(contentHtml)
                        .build()
        );
    }
    public void createCategory(String category) {
        if(!postRepository.existsByCategory(category)) {
            postRepository.save(
                    Posts.builder()
                            .category(category)
                            .build()
            );
        }
    }
}

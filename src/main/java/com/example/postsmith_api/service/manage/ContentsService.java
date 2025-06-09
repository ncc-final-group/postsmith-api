package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Blogs;
import com.example.postsmith_api.domain.blog.Categories;
import com.example.postsmith_api.domain.blog.Contents;
import com.example.postsmith_api.domain.blog.PostType;
import com.example.postsmith_api.dto.PostDto;
import com.example.postsmith_api.repository.manage.CategoryRepository;
import com.example.postsmith_api.repository.manage.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentsService {
    private final ContentsRepository postRepository;
    private final CategoryRepository categoryRepository;
    public void createPost(Blogs blog, PostDto dto) {
        Categories category = categoryRepository.findById(dto.getCategory())
                .orElse(null);
        String plainContent = dto.getContent().replaceAll("<[^>]*>", "");
        System.out.println(dto.getContent());
        System.out.println(plainContent);
        int latestPostId = postRepository.findLatestContentIdByBlogId(blog);
        postRepository.save(
                Contents.builder()
                        .blogId(blog)
                        .categoryId(category)
                        .sequence(latestPostId)
                        .title(dto.getTitle())
                        .postType(PostType.fromString(dto.getPostType()))
                        .contentHtml(dto.getContent())
                        .contentPlain(plainContent)
                        .build()
        );
    }
}

package com.postsmith.api.service;

import com.postsmith.api.dto.BlogsDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.repository.BlogsRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogsService {

    private final BlogsRepository blogsRepository;

    // userId로 운영중인 블로그 정보
    public List<BlogsDto> getBlogsByUserId(Integer userId) {
        List<BlogsEntity> blogs = blogsRepository.findAllByUser_Id(userId);
        return blogs.stream()
                .map(BlogsDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // blog_id로 블로그 정보
    public ResponseEntity<BlogsDto> getBlogById(Integer id) {
        BlogsEntity entity = blogsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        BlogsDto dto = BlogsDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    
    // 블로그 정보 업데이트 
    public void updateBlog(Integer id, BlogsDto dto) {
        int updated = blogsRepository.updateBlog (
            id,
            dto.getName(),
            dto.getNickname(),
            dto.getDescription()
        );

        if (updated == 0) {
            throw new RuntimeException("해당 블로그가 존재하지 않습니다.");
        }
    }

    // 추천 구독 블로그
    public List<BlogsDto> findrecommendedBlogs(Integer userId) {
        List<BlogsEntity> blogs = blogsRepository.findrecommendedBlogs(userId);
        return blogs.stream()
                .map(BlogsDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 구독
    public void subscribeBlog(Integer subscriberId, Integer blogId) {
        int inserted = blogsRepository.insertSubscription(subscriberId, blogId);

        if (inserted == 0) {
            throw new RuntimeException("해당 블로그가 존재하지 않습니다.");
        }
    }
}

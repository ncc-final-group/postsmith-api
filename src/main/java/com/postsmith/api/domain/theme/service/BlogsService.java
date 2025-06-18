package com.postsmith.api.domain.theme.service;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogsService {

	private final BlogsRepository blogsRepository;

	// 추천 구독 블로그
	public List<BlogsDto> findrecommendedBlogs(Integer userId) {
		List<BlogsEntity> blogs = blogsRepository.findrecommendedBlogs(userId);
		return blogs.stream().map(BlogsDto::fromEntity).collect(Collectors.toList());
	}

	// 구독
	public void subscribeBlog(Integer subscriberId, Integer blogId) {
		int inserted = blogsRepository.insertSubscription(subscriberId, blogId);

		if (inserted == 0) {
			throw new RuntimeException("해당 블로그가 존재하지 않습니다.");
		}
	}
}

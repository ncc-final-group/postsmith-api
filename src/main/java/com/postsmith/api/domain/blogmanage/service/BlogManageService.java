package com.postsmith.api.domain.blogmanage.service;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.UsersRepository;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogManageService {
	private final BlogsRepository blogsRepository;
	private final UsersRepository usersRepository;

	// userId로 운영중인 블로그 정보
	public List<BlogsDto> getBlogsByUserId(Integer userId) {
		List<BlogsEntity> blogs = blogsRepository.findAllByUser_Id(userId);
		return blogs.stream().map(BlogsDto::fromEntity).collect(Collectors.toList());
	}

	// blog_id로 블로그 정보
	public ResponseEntity<BlogsDto> getBlogById(Integer id) {
		BlogsEntity entity = blogsRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
		BlogsDto dto = BlogsDto.fromEntity(entity);
		return ResponseEntity.ok(dto);
	}

	// 블로그 정보 업데이트
	public void updateBlog(Integer id, BlogsDto dto) {
		int updated = blogsRepository.updateBlog(id, dto.getName(), dto.getNickname(), dto.getDescription(), dto.getLogoImage());

		if (updated == 0) {
			throw new RuntimeException("해당 블로그가 존재하지 않습니다.");
		}
	}

	// 블로그 삭제
	public boolean deleteBlog(Integer blogId) {
		if (!blogsRepository.existsById(blogId)) {
			return false;
		}
		blogsRepository.deleteById(blogId);
		return true;
	}

	@Transactional
	public BlogsDto create(BlogsDto blogDto, Integer userId) {
		UsersEntity user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		BlogsEntity blogEntity = blogDto.toEntity(user);
		BlogsEntity saved = blogsRepository.save(blogEntity);
		return BlogsDto.fromEntity(saved);
	}

}

package com.postsmith.api.domain.blogmanage.controller;

import com.postsmith.api.domain.blogmanage.service.BlogManageService;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogmanage")
@RequiredArgsConstructor
public class BlogManageController {

	private final BlogManageService blogManageService;

	// userId로 운영 중인 블로그 정보
	@GetMapping("/userId/{userId}")
	public ResponseEntity<List<BlogsDto>> getBlogsByUserId(@PathVariable("userId") Integer userId) {
		List<BlogsDto> blogList = blogManageService.getBlogsByUserId(userId);
		return ResponseEntity.ok(blogList);
	}

	// blog_id로 블로그 정보
	@GetMapping("/{id}")
	public ResponseEntity<BlogsDto> getBlogById(@PathVariable("id") Integer id) {
		return blogManageService.getBlogById(id);
	}

	// 블로그 정보 업데이트
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateBlog(@PathVariable("id") Integer id, @RequestBody BlogsDto dto) {
		blogManageService.updateBlog(id, dto);
		System.out.println(dto.getLogoImage());
		return ResponseEntity.ok().build();
	}

	// 블로그 삭제
	@DeleteMapping("/delete/blogId/{blogId}")
	public ResponseEntity<?> deleteBlog(@PathVariable("blogId") Integer blogId) {
		boolean deleted = blogManageService.deleteBlog(blogId);
		return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

	@PostMapping("/create/userId/{userId}")
	public ResponseEntity<BlogsDto> createBlog(@PathVariable("userId") Integer userId, @RequestBody BlogsDto blogDto) {
		BlogsDto created = blogManageService.create(blogDto, userId);
		System.out.println(blogDto.toString());
		return ResponseEntity.ok(created);
	}

}

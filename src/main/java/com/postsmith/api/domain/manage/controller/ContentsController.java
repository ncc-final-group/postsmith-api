package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.blog.BlogService;
import com.postsmith.api.domain.manage.dto.ContentsRequestDto;
import com.postsmith.api.domain.manage.dto.ContentsResponseDto;
import com.postsmith.api.domain.manage.service.ContentsService;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ContentsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor

@Slf4j
public class ContentsController {
	private final ContentsService contentsService;
	private final BlogService blogService;

	@PostMapping("/blog/{blogAddress}/create")
	public ResponseEntity<?> createContents(@RequestBody ContentsRequestDto dto, @PathVariable("blogAddress") String blogAddress) {
		try {
			BlogsEntity blog = blogService.findBlogByAddress(blogAddress);
			ContentsResponseDto content = contentsService.createPost(blog, dto);
			return ResponseEntity.ok(content);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found: {}", blogAddress, e);
			return ResponseEntity.badRequest().body("Blog not found with address: " + blogAddress);
		} catch (Exception e) {
			log.error("Error creating post: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
		}
	}

	@GetMapping("/blog/{blogAddress}")
	public ResponseEntity<List<ContentsResponseDto>> getBlogContents(@PathVariable("blogAddress") String blogAddress, @RequestParam(defaultValue = "false") boolean publicOnly) {
		try {
			BlogsEntity blog = blogService.findBlogByAddress(blogAddress);
			List<ContentsResponseDto> contents;

			if (publicOnly) {
				contents = contentsService.getPublicBlogContents(blog);
			} else {
				contents = contentsService.getBlogContents(blog);
			}

			return ResponseEntity.ok(contents);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found: {}", blogAddress, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting blog contents: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/blog/{blogAddress}/{sequence}")
	public ResponseEntity<?> getContentBySequence(@PathVariable("blogAddress") String blogAddress, @PathVariable("sequence") Integer sequence) {
		try {
			BlogsEntity blog = blogService.findBlogByAddress(blogAddress);
			ContentsResponseDto content = contentsService.getContentBySequence(blog, sequence);
			return ResponseEntity.ok(content);
		} catch (IllegalArgumentException e) {
			log.error("Content not found: blog={}, sequence={}", blogAddress, sequence, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting content: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getContentById(@PathVariable("id") Integer id) {
		try {
			ContentsResponseDto content = contentsService.getContentById(id).toDto();
			return ResponseEntity.ok(content);
		} catch (IllegalArgumentException e) {
			log.error("Content not found with id: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting content: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateContent(@PathVariable("id") Integer id, @RequestBody ContentsRequestDto dto) {
		try {
			ContentsResponseDto content = contentsService.updateContent(id, dto);
			return ResponseEntity.ok(content);
		} catch (IllegalArgumentException e) {
			log.error("Content not found with id: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error updating content: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error updating content: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteContent(@PathVariable("id") Integer id) {
		try {
			contentsService.deleteContent(id);
			return ResponseEntity.ok().body("Content deleted successfully");
		} catch (IllegalArgumentException e) {
			log.error("Content not found with id: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error deleting content: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error deleting content: " + e.getMessage());
		}
	}

	@GetMapping("/blog/{blogAddress}/search")
	public ResponseEntity<List<ContentsEntity>> searchContents(@PathVariable("blogAddress") String blogAddress, @RequestParam("keyword") String keyword) {
		try {
			BlogsEntity blog = blogService.findBlogByAddress(blogAddress);
			List<ContentsEntity> contents = contentsService.searchContents(blog, keyword);
			return ResponseEntity.ok(contents);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found: {}", blogAddress, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error searching contents: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/blog/{blogId}/drafts")
	public ResponseEntity<List<ContentsResponseDto>> getDraftContents(
			@PathVariable("blogId") Integer blogId,
			@RequestParam(required = false) String type) {
		try {
			BlogsEntity blog = blogService.findBlogById(blogId);
			List<ContentsResponseDto> drafts = contentsService.getDraftContents(blog, type);
			return ResponseEntity.ok(drafts);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found with id: {}", blogId, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting draft contents: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}
}

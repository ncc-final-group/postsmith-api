package com.postsmith.api.domain.content.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postsmith.api.domain.content.dto.PostsDto;
import com.postsmith.api.domain.content.dto.PrivacyUpdateRequestDto;
import com.postsmith.api.domain.content.service.PostsService;

import com.postsmith.api.entity.ContentsEntity;

@RestController
@RequestMapping("/api/Posts")
public class PostsController {

	private final PostsService contentService;

	public PostsController(PostsService contentService) {
		this.contentService = contentService;
	}

	@GetMapping("/{type}")
	public List<PostsDto> getUserContentsByType(@PathVariable("type") String type, @RequestParam("blogId") Long blogId // ✅ 추가
	) {
		ContentsEntity.ContentEnum contentType;

		switch (type.toLowerCase()) {
		case "post":
			contentType = ContentsEntity.ContentEnum.POSTS;
			break;
		case "page":
			contentType = ContentsEntity.ContentEnum.PAGE;
			break;
		case "notice":
			contentType = ContentsEntity.ContentEnum.NOTICE;
			break;
		default:
			throw new IllegalArgumentException("Invalid content type: " + type);
		}

		return contentService.getPostsByTypeAndBlogId(contentType, blogId);
	}
	
	
	
	@GetMapping("/blogid")
	public ResponseEntity<Map<String, Integer>> getBlogIdByAddress(@RequestParam("address") String address) {
	    Integer blogId = contentService.findBlogIdByAddress(address);
	    Map<String, Integer> response = new HashMap<>();
	    response.put("blogId", blogId);
	    return ResponseEntity.ok(response);
	}

	

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteContent(@PathVariable("id") Integer id) {
		contentService.deletePostById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteMultiple(@RequestBody List<Integer> ids) {
		contentService.deletePostsByIds(ids);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/privacy")
	public ResponseEntity<Void> updatePrivacy(@PathVariable("id") Integer id, @RequestParam("isPublic") boolean isPublic) {

		contentService.updatePrivacy(id, isPublic);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/privacy")
	public ResponseEntity<Void> updatePrivacy(@RequestBody PrivacyUpdateRequestDto request) {
		contentService.updateIsPublicForContents(request.getContentIds(), request.getIsPublic());
		return ResponseEntity.ok().build();
	}

}

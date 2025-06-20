package com.postsmith.api.domain.content.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.postsmith.api.domain.content.dto.RepliesManageDto;
import com.postsmith.api.domain.content.dto.RepliesManagePostDto;
import com.postsmith.api.domain.content.service.RepliesManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/replies")
public class RepliesManageController {

	private final RepliesManageService repliesManageService;

	public RepliesManageController(RepliesManageService repliesManageService) {
		this.repliesManageService = repliesManageService;
	}

	@GetMapping("/{blogId}")
	public List<RepliesManageDto> getReplies(@PathVariable("blogId") Long blogId) {
	    return repliesManageService.getRepliesByBlogId(blogId);
	}

	@PostMapping("/submitReply")
	@ResponseBody
	public ResponseEntity<String> postReply(@RequestBody RepliesManagePostDto dto) {
		try {
			repliesManageService.saveReply(dto);
			return ResponseEntity.ok("등록 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteContent(@PathVariable("id") Integer id) {
		repliesManageService.deleteReplyById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteMultiple(@RequestBody List<Integer> ids) {
		repliesManageService.deleteRepliesByIds(ids);
		return ResponseEntity.noContent().build();
	}

}

package com.postsmith.api.domain.theme.controller;

import com.postsmith.api.domain.theme.dto.TagsDto;
import com.postsmith.api.domain.theme.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagsController {

	private final TagsService tagsService;

	// 모든 태
	@GetMapping
	public ResponseEntity<List<TagsDto>> getAllThemes() {
		return ResponseEntity.ok(tagsService.getAllTags());
	}

}

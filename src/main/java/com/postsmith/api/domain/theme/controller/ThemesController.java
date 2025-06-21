package com.postsmith.api.domain.theme.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postsmith.api.domain.theme.dto.BlogThemesDto;
import com.postsmith.api.domain.theme.dto.BlogsDto;
import com.postsmith.api.domain.theme.dto.ThemeTagsDto;
import com.postsmith.api.domain.theme.dto.ThemesDto;
import com.postsmith.api.domain.theme.service.ThemesService;

import java.util.List;


@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Slf4j
public class ThemesController {

	private final ThemesService themesService;

	// 특정 테마
	@GetMapping("/{id}")
	public ResponseEntity<ThemesDto> getThemeById(@PathVariable("id") Integer id) {
		return themesService.getThemeById(id);
	}

	// 모든 테마 - ThemesDto 리스트 반환
	@GetMapping("/a")
	public ResponseEntity<List<ThemesDto>> getAllThemes() {
		return ResponseEntity.ok(themesService.getAllThemes());
	}
	
	// 모든 테마 - 태그랑 같
	@GetMapping("/withTag")
	public ResponseEntity<List<ThemeTagsDto>> getAllThemesWithTag() {
		return ResponseEntity.ok(themesService.getAllThemesWithTag());
	}
	
	// 테마 적용
	@PostMapping("/applyTheme/blogId/{blogId}/themeId/{themeId}")
	public ResponseEntity<BlogThemesDto> applyTheme(@PathVariable("blogId") Integer blogId, @PathVariable("themeId") Integer themeId) {
	    BlogThemesDto created = themesService.create(blogId, themeId);
	    return ResponseEntity.ok(created);
	}
}

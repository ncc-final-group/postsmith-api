package com.postsmith.api.domain.theme.service;

import com.postsmith.api.domain.theme.dto.ThemeTagsDto;
import com.postsmith.api.domain.theme.dto.ThemesDto;
import com.postsmith.api.entity.TagsEntity;
import com.postsmith.api.entity.ThemesEntity;
import com.postsmith.api.repository.ThemeTagsRepository;
import com.postsmith.api.repository.ThemesRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemesService {
	private final ThemesRepository themesRepository;
	private final ThemeTagsRepository themeTagsRepository;

	// 특정 테마
	public ResponseEntity<ThemesDto> getThemeById(Integer id) {
		ThemesEntity entity = themesRepository.findById(id).orElseThrow(() -> new RuntimeException("Theme not found"));
		ThemesDto dto = ThemesDto.fromEntity(entity);
		return ResponseEntity.ok(dto);
	}

	// 모든 테마
	public List<ThemeTagsDto> getAllThemes() {
		List<Object[]> results = themeTagsRepository.findThemeTagsOrderByThemeCreatedAtDesc();

		return results.stream().map(row -> ThemeTagsDto.fromEntity((ThemesEntity) row[0], (TagsEntity) row[1])).collect(Collectors.toList());
	}
}

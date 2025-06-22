package com.postsmith.api.domain.theme.service;

import com.postsmith.api.domain.theme.dto.BlogThemesDto;
import com.postsmith.api.domain.theme.dto.BlogsDto;
import com.postsmith.api.domain.theme.dto.ThemeTagsDto;
import com.postsmith.api.domain.theme.dto.ThemesDto;
import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.TagsEntity;
import com.postsmith.api.entity.ThemesEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.BlogThemesRepository;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.ThemeTagsRepository;
import com.postsmith.api.repository.ThemesRepository;

import jakarta.transaction.Transactional;
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
	private final BlogsRepository blogsRepository;
	private final BlogThemesRepository blogThemesRepository;
	

	// 특정 테마
	public ResponseEntity<ThemesDto> getThemeById(Integer id) {
		ThemesEntity entity = themesRepository.findById(id).orElseThrow(() -> new RuntimeException("Theme not found"));
		ThemesDto dto = ThemesDto.fromEntity(entity);
		return ResponseEntity.ok(dto);
	}

	// 모든 테마
	public List<ThemesDto> getAllThemes() {
		List<ThemesEntity> results = themesRepository.findAll();
		return results.stream()
			.map(ThemesDto::fromEntity)
			.collect(Collectors.toList());
	}
	
	public List<ThemeTagsDto> getAllThemesWithTag() {
		List<Object[]> results = themeTagsRepository.findThemeTagsOrderByThemeCreatedAtDesc();

		return results.stream().map(row -> ThemeTagsDto.fromEntity((ThemesEntity) row[0], (TagsEntity) row[1])).collect(Collectors.toList());
	}
	
	@Transactional
	public BlogThemesDto create(Integer blogId, Integer themeId) {
	    BlogsEntity blog = blogsRepository.findById(blogId)
	        .orElseThrow(() -> new RuntimeException("블로그를 찾을 수 없습니다."));
	    ThemesEntity theme = themesRepository.findById(themeId)
	        .orElseThrow(() -> new RuntimeException("테마를 찾을 수 없습니다."));

	    // 기존 활성 테마 조회
	    List<BlogThemesEntity> existingActiveThemes = blogThemesRepository.findByBlogAndIsActiveTrue(blog)
	        .stream().toList();

	    // 동일한 테마가 이미 활성화되어 있는지 확인
	    for (BlogThemesEntity existingTheme : existingActiveThemes) {
	        if (existingTheme.getTheme().getId().equals(themeId)) {
	            // 이미 같은 테마가 활성화되어 있으면 그대로 반환
	        return new BlogThemesDto(
	            existingTheme.getId(),
	            blogId,
	            themeId,
	            existingTheme.getThemeSetting(),
	            existingTheme.getIsActive()
	        );
	    }
	    }

	    // 기존 활성 테마가 있으면 업데이트, 없으면 새로 생성
	    if (!existingActiveThemes.isEmpty()) {
	        // 첫 번째 활성 테마를 새로운 테마로 업데이트
	        BlogThemesEntity existingTheme = existingActiveThemes.get(0);
	        existingTheme.updateTheme(theme);
	        existingTheme.updateThemeHtml(theme.getHtml());
	        existingTheme.updateThemeCss(theme.getCss());
	        existingTheme.updateThemeSetting("");
	        
	        // 나머지 활성 테마들은 비활성화
	        for (int i = 1; i < existingActiveThemes.size(); i++) {
	            existingActiveThemes.get(i).updateIsActive(false);
	            blogThemesRepository.save(existingActiveThemes.get(i));
	        }
	        
	        BlogThemesEntity savedTheme = blogThemesRepository.save(existingTheme);
	        
	        return new BlogThemesDto(
	            savedTheme.getId(),
	            blogId,
	            themeId,
	            savedTheme.getThemeSetting(),
	            savedTheme.getIsActive()
	        );
	    } else {
	        // 활성 테마가 없으면 새로 생성
	    BlogThemesEntity newTheme = BlogThemesEntity.builder()
	        .blog(blog)
	        .theme(theme)
	            .themeHtml(theme.getHtml())
	            .themeCss(theme.getCss())
	            .themeSetting("")
	        .isActive(true)
	        .build();

	        BlogThemesEntity savedTheme = blogThemesRepository.save(newTheme);

	    return new BlogThemesDto(
	            savedTheme.getId(),
	        blogId,
	        themeId,
	            savedTheme.getThemeSetting(),
	            savedTheme.getIsActive()
	    );
	    }
	}


}

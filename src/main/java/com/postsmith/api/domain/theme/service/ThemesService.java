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
	public List<ThemeTagsDto> getAllThemes() {
		List<Object[]> results = themeTagsRepository.findThemeTagsOrderByThemeCreatedAtDesc();

		return results.stream().map(row -> ThemeTagsDto.fromEntity((ThemesEntity) row[0], (TagsEntity) row[1])).collect(Collectors.toList());
	}
	
	@Transactional
	public BlogThemesDto create(Integer blogId, Integer themeId) {
	    BlogsEntity blog = blogsRepository.findById(blogId)
	        .orElseThrow(() -> new RuntimeException("블로그를 찾을 수 없습니다."));
	    ThemesEntity theme = themesRepository.findById(themeId)
	        .orElseThrow(() -> new RuntimeException("테마를 찾을 수 없습니다."));

	    List<BlogThemesEntity> existing = blogThemesRepository.findByBlogAndIsActiveTrue(blog)
	        .stream()
	        .filter(bt -> bt.getTheme().getId().equals(themeId))
	        .toList();

	    if (!existing.isEmpty()) {
	        BlogThemesEntity existingTheme = existing.get(0);
	        return new BlogThemesDto(
	            existingTheme.getId(),
	            blogId,
	            themeId,
	            existingTheme.getThemeSetting(),
	            existingTheme.getIsActive()
	        );
	    }

	    blogThemesRepository.deactivateAllThemesByBlog(blog);

	    BlogThemesEntity newTheme = BlogThemesEntity.builder()
	        .blog(blog)
	        .theme(theme)
	        .themeSetting(theme.getHtml())
	        .isActive(true)
	        .build();

	    blogThemesRepository.save(newTheme);

	    return new BlogThemesDto(
	        newTheme.getId(),
	        blogId,
	        themeId,
	        newTheme.getThemeSetting(),
	        newTheme.getIsActive()
	    );
	}


}

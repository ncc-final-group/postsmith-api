package com.postsmith.api.domain.manage.service;

import com.postsmith.api.domain.blog.BlogService;
import com.postsmith.api.domain.manage.dto.BlogThemesDto;
import com.postsmith.api.domain.theme.service.ThemesService;
import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ThemesEntity;
import com.postsmith.api.repository.BlogThemesRepository;
import com.postsmith.api.repository.ThemesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogThemesService {
    
    private final BlogThemesRepository blogThemesRepository;
    private final BlogService blogService;
    private final ThemesRepository themesRepository;
    
    // 블로그의 현재 테마 조회
    @Transactional(readOnly = true)
    public BlogThemesDto getBlogActiveTheme(Integer blogId) {
        try {
            Optional<BlogThemesEntity> activeTheme = blogThemesRepository.findActiveByBlogId(blogId);
            if (activeTheme.isPresent()) {
                return BlogThemesDto.fromEntity(activeTheme.get());
            }
            return null;
        } catch (Exception e) {
            log.error("Error getting blog active theme: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get blog active theme: " + e.getMessage(), e);
        }
    }
    
    // 블로그에 테마 적용 (새로운 테마 선택)
    @Transactional
    public BlogThemesDto applyThemeToBlog(Integer blogId, Integer themeId) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            ThemesEntity theme = themesRepository.findById(themeId)
                    .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + themeId));

            Optional<BlogThemesEntity> activeTheme = blogThemesRepository.findByBlogAndIsActiveTrue(blog);
            if(activeTheme.isPresent()) {
                // 이미 활성화된 테마가 있다면 비활성화
                BlogThemesEntity existingTheme = activeTheme.get();
                existingTheme.updateTheme(theme);
                return BlogThemesDto.fromEntity(blogThemesRepository.save(existingTheme));
            }else{
                BlogThemesEntity newBlogTheme = BlogThemesEntity.builder()
                        .blog(blog)
                        .theme(theme)
                        .themeHtml(theme.getHtml())
                        .themeCss(theme.getCss())
                        .themeSetting("")
                        .isActive(true)
                        .build();
                return BlogThemesDto.fromEntity(blogThemesRepository.save(newBlogTheme));
            }
        } catch (Exception e) {
            log.error("Error applying theme to blog: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to apply theme to blog: " + e.getMessage(), e);
        }
    }
    
    // 블로그 테마의 HTML/CSS 커스텀 수정
    @Transactional
    public BlogThemesDto updateBlogThemeContent(Integer blogId, String themeHtml, String themeCss) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            
            // 현재 활성 테마 조회
            BlogThemesEntity activeTheme = blogThemesRepository.findByBlogAndIsActiveTrue(blog)
                    .orElseThrow(() -> new IllegalArgumentException("No active theme found for blog: " + blogId));
            
            // 기존 테마 엔티티 업데이트 (삭제-생성 대신 직접 수정)
            activeTheme.updateThemeContent(themeHtml, themeCss);
            
            // 업데이트된 테마 저장 (동일한 엔티티)
            BlogThemesEntity savedTheme = blogThemesRepository.save(activeTheme);
            
            log.info("Blog theme content updated: blogId={}", blogId);
            
            return BlogThemesDto.fromEntity(savedTheme);
            
        } catch (Exception e) {
            log.error("Error updating blog theme content: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update blog theme content: " + e.getMessage(), e);
        }
    }
    
    // 블로그 테마 설정 업데이트
    @Transactional
    public BlogThemesDto updateBlogThemeSetting(Integer blogId, String themeSetting) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            
            // 현재 활성 테마 조회
            BlogThemesEntity activeTheme = blogThemesRepository.findByBlogAndIsActiveTrue(blog)
                    .orElseThrow(() -> new IllegalArgumentException("No active theme found for blog: " + blogId));
            
            // 기존 테마 엔티티 업데이트 (삭제-생성 대신 직접 수정)
            activeTheme.updateThemeSetting(themeSetting);
            
            // 업데이트된 테마 저장 (동일한 엔티티)
            BlogThemesEntity savedTheme = blogThemesRepository.save(activeTheme);
            
            log.info("Blog theme setting updated: blogId={}", blogId);
            
            return BlogThemesDto.fromEntity(savedTheme);
            
        } catch (Exception e) {
            log.error("Error updating blog theme setting: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update blog theme setting: " + e.getMessage(), e);
        }
    }
} 
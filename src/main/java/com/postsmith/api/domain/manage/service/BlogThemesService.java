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
            
            // 기존 테마들 모두 비활성화
            blogThemesRepository.deactivateAllByBlog(blog);
            
            // 새 테마 적용 (원본 HTML/CSS 사용)
            BlogThemesEntity newBlogTheme = BlogThemesEntity.builder()
                    .blog(blog)
                    .theme(theme)
                    .themeHtml(theme.getHtml()) // 원본 HTML
                    .themeCss(theme.getCss())   // 원본 CSS
                    .themeSetting(null)
                    .isActive(true)
                    .build();
            
            BlogThemesEntity savedTheme = blogThemesRepository.save(newBlogTheme);
            log.info("Theme applied to blog: blogId={}, themeId={}", blogId, themeId);
            
            return BlogThemesDto.fromEntity(savedTheme);
            
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
            
            // 새로운 BlogThemesEntity 생성 (기존 것을 업데이트하는 방식)
            BlogThemesEntity updatedTheme = BlogThemesEntity.builder()
                    .blog(activeTheme.getBlog())
                    .theme(activeTheme.getTheme())
                    .themeHtml(themeHtml != null ? themeHtml : activeTheme.getThemeHtml())
                    .themeCss(themeCss != null ? themeCss : activeTheme.getThemeCss())
                    .themeSetting(activeTheme.getThemeSetting())
                    .isActive(true)
                    .build();
            
            // 기존 테마 삭제
            blogThemesRepository.delete(activeTheme);
            
            // 새로운 테마 저장
            BlogThemesEntity savedTheme = blogThemesRepository.save(updatedTheme);
            
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
            
            // 새로운 BlogThemesEntity 생성
            BlogThemesEntity updatedTheme = BlogThemesEntity.builder()
                    .blog(activeTheme.getBlog())
                    .theme(activeTheme.getTheme())
                    .themeHtml(activeTheme.getThemeHtml())
                    .themeCss(activeTheme.getThemeCss())
                    .themeSetting(themeSetting)
                    .isActive(true)
                    .build();
            
            // 기존 테마 삭제
            blogThemesRepository.delete(activeTheme);
            
            // 새로운 테마 저장
            BlogThemesEntity savedTheme = blogThemesRepository.save(updatedTheme);
            
            log.info("Blog theme setting updated: blogId={}", blogId);
            
            return BlogThemesDto.fromEntity(savedTheme);
            
        } catch (Exception e) {
            log.error("Error updating blog theme setting: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update blog theme setting: " + e.getMessage(), e);
        }
    }
} 
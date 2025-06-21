package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.manage.dto.BlogThemesDto;
import com.postsmith.api.domain.manage.service.BlogThemesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/manage/blog-themes")
@RequiredArgsConstructor
@Slf4j
public class BlogThemesController {
    
    private final BlogThemesService blogThemesService;
    
    // 블로그의 현재 테마 조회
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<BlogThemesDto> getBlogActiveTheme(@PathVariable Integer blogId) {
        try {
            BlogThemesDto activeTheme = blogThemesService.getBlogActiveTheme(blogId);
            if (activeTheme != null) {
                return ResponseEntity.ok(activeTheme);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting blog active theme: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 블로그에 테마 적용
    @PostMapping("/blog/{blogId}/apply-theme")
    public ResponseEntity<BlogThemesDto> applyThemeToBlog(
            @PathVariable Integer blogId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer themeId = request.get("themeId");
            if (themeId == null) {
                return ResponseEntity.badRequest().build();
            }
            
            BlogThemesDto result = blogThemesService.applyThemeToBlog(blogId, themeId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error applying theme to blog: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 블로그 테마의 HTML/CSS 수정
    @PutMapping("/blog/{blogId}/content")
    public ResponseEntity<BlogThemesDto> updateBlogThemeContent(
            @PathVariable Integer blogId,
            @RequestBody Map<String, String> request) {
        try {
            String themeHtml = request.get("themeHtml");
            String themeCss = request.get("themeCss");
            
            if (themeHtml == null && themeCss == null) {
                return ResponseEntity.badRequest().build();
            }
            
            BlogThemesDto result = blogThemesService.updateBlogThemeContent(blogId, themeHtml, themeCss);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating blog theme content: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 블로그 테마 설정 수정
    @PutMapping("/blog/{blogId}/setting")
    public ResponseEntity<BlogThemesDto> updateBlogThemeSetting(
            @PathVariable Integer blogId,
            @RequestBody Map<String, String> request) {
        try {
            String themeSetting = request.get("themeSetting");
            if (themeSetting == null) {
                return ResponseEntity.badRequest().build();
            }
            
            BlogThemesDto result = blogThemesService.updateBlogThemeSetting(blogId, themeSetting);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating blog theme setting: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
} 
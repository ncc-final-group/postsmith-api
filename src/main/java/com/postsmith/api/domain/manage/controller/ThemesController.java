package com.postsmith.api.domain.manage.controller;

import com.postsmith.api.domain.manage.service.ThemesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/themes")
public class ThemesController {
    private final ThemesService themesService;
    @GetMapping("/my-themes/{userId}")
    public ResponseEntity<?> findThemesByBlogId(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(themesService.findThemesByBlogId(userId));
    }
    @GetMapping("/list")
    public ResponseEntity<?> getThemeList(){
        return ResponseEntity.ok(themesService.getAllThemes());
    }
    @GetMapping("/{themeId}")
    public ResponseEntity<?> getThemeById(@PathVariable("themeId") Integer themeId) {
        return ResponseEntity.ok(themesService.getThemeById(themeId));
    }
}

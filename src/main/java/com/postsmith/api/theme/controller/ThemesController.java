package com.postsmith.api.theme.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postsmith.api.theme.dto.ThemeTagsDto;
import com.postsmith.api.theme.dto.ThemesDto;
import com.postsmith.api.theme.service.ThemesService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemesController {

    private final ThemesService themesService;

    // 특정 테마
    @GetMapping("/{id}")
    public ResponseEntity<ThemesDto> getThemeById(@PathVariable("id") Integer id) {
        return themesService.getThemeById(id);
    }

    // 모든 테마
    @GetMapping("/a")
    public ResponseEntity<List<ThemeTagsDto>> getAllThemes() {
        return ResponseEntity.ok(themesService.getAllThemes());
    }
}

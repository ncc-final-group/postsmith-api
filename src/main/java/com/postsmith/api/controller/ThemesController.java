package com.postsmith.api.controller;

import com.postsmith.api.dto.ThemesDto;
import com.postsmith.api.service.ThemesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<List<ThemesDto>> getAllThemes() {
        return ResponseEntity.ok(themesService.getAllThemes());
    }
}

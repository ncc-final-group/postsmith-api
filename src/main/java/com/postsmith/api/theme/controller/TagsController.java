package com.postsmith.api.theme.controller;

import com.postsmith.api.theme.dto.TagsDto;
import com.postsmith.api.theme.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    // 모든 태
    @GetMapping
    public ResponseEntity<List<TagsDto>> getAllThemes() {
        return ResponseEntity.ok(tagsService.getAllTags());
    }
    
}

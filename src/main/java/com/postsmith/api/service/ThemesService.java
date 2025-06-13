package com.postsmith.api.service;

import com.postsmith.api.dto.ThemesDto;
import com.postsmith.api.entity.ThemesEntity;
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
    
    // 특정 테마
    public ResponseEntity<ThemesDto> getThemeById(Integer id) {
        ThemesEntity entity = themesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theme not found"));
        ThemesDto dto = ThemesDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    
    // 모든 테마
    public List<ThemesDto> getAllThemes() {
        return themesRepository.findAll()
                .stream()
                .map(ThemesDto::fromEntity)
                .collect(Collectors.toList());
    }
}

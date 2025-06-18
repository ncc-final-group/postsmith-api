package com.postsmith.api.domain.manage.service;

import com.postsmith.api.domain.manage.dto.ThemesDto;
import com.postsmith.api.entity.ThemesEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.ThemesRepository;
import com.postsmith.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemesService {
    private final ThemesRepository themesRepository;
    private final BlogsRepository blogsRepository;
    // 테마 목록 조회
    public List<ThemesDto> getAllThemes() {
        return themesRepository.findAll().stream().map(theme -> theme.toDto()).collect(Collectors.toList());
    }

    public ThemesDto findThemesByBlogId(Integer blogId) {
        ThemesEntity myTheme = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId))
                .getTheme();
        return myTheme.toDto();
    }

    public Object getThemeById(Integer themeId) {
        return themesRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with ID: " + themeId))
                .toDto();
    }
}

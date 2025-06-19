package com.postsmith.api.domain.theme.dto;

import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ThemesEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BlogThemesDto {
    private Integer id;
    private Integer blogId;
    private Integer themeId;
    private String themeSetting;
    private Boolean isActive;

    @Builder
    public BlogThemesDto(Integer id, Integer blogId, Integer themeId, String themeSetting, Boolean isActive) {
        this.id = id;
        this.blogId = blogId;
        this.themeId = themeId;
        this.themeSetting = themeSetting;
        this.isActive = isActive;
    }

    public static BlogThemesDto fromEntity(BlogThemesEntity entity) {
        return BlogThemesDto.builder()
            .id(entity.getId())
            .blogId(entity.getBlog().getId())
            .themeId(entity.getTheme().getId())
            .themeSetting(entity.getThemeSetting())
            .isActive(entity.getIsActive())
            .build();
    }

    public BlogThemesEntity toEntity(BlogsEntity blog, ThemesEntity theme) {
        return BlogThemesEntity.builder()
            .blog(blog)
            .theme(theme)
            .themeSetting(this.themeSetting)
            .isActive(this.isActive)
            .build();
    }
}

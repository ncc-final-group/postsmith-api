package com.postsmith.api.domain.manage.dto;

import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ThemesEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogThemesDto {
    private Integer id;
    private Integer blogId;
    private Integer themeId;
    private String themeName;
    private String themeHtml;
    private String themeCss;
    private String themeSetting;
    private Boolean isActive;
    
    public static BlogThemesDto fromEntity(BlogThemesEntity entity) {
        return BlogThemesDto.builder()
                .id(entity.getId())
                .blogId(entity.getBlog() != null ? entity.getBlog().getId() : null)
                .themeId(entity.getTheme() != null ? entity.getTheme().getId() : null)
                .themeName(entity.getTheme() != null ? entity.getTheme().getName() : null)
                .themeHtml(entity.getThemeHtml())
                .themeCss(entity.getThemeCss())
                .themeSetting(entity.getThemeSetting())
                .isActive(entity.getIsActive())
                .build();
    }
    
    public BlogThemesEntity toEntity(BlogsEntity blog, ThemesEntity theme) {
        return BlogThemesEntity.builder()
                .blog(blog)
                .theme(theme)
                .themeHtml(this.themeHtml)
                .themeCss(this.themeCss)
                .themeSetting(this.themeSetting)
                .isActive(this.isActive)
                .build();
    }
} 
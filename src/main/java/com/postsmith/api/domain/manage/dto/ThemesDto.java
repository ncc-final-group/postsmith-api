package com.postsmith.api.domain.manage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThemesDto {
    private Integer id;
    private String name;
    private String description;
    private String thumbnailImage;
    private String themeHtml;
    private String themeCss;

    @Override
    public String toString() {
        return "ThemesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailImage='" + thumbnailImage + '\'' +
                '}';
    }
}

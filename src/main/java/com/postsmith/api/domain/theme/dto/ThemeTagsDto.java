package com.postsmith.api.domain.theme.dto;

import com.postsmith.api.entity.TagsEntity;
import com.postsmith.api.entity.ThemesEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeTagsDto {
	private ThemesDto theme;
	private TagsDto tag;

	public static ThemeTagsDto fromEntity(ThemesEntity themesEntity, TagsEntity tagsEntity) {
		return ThemeTagsDto.builder().theme(ThemesDto.fromEntity(themesEntity)).tag(TagsDto.fromEntity(tagsEntity)).build();
	}
}

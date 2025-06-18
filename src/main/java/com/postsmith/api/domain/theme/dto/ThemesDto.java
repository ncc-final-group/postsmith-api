package com.postsmith.api.domain.theme.dto;

import com.postsmith.api.entity.ThemesEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemesDto {
	private Integer id;
	private String name;
	private String coverImage;
	private String image;
	private String description;
	private String author;
	private String authorLink;
	private String html;
	private String css;

	public static ThemesDto fromEntity(ThemesEntity entity) {
		return ThemesDto.builder().id(entity.getId()).name(entity.getName()).coverImage(entity.getCoverImage()).image(entity.getImage()).description(entity.getDescription())
				.author(entity.getAuthor()).authorLink(entity.getAuthorLink()).html(entity.getHtml()).css(entity.getCss()).build();
	}

	public ThemesEntity toEntity() {
		return ThemesEntity.builder().name(this.name).coverImage(this.coverImage).image(this.image).description(this.description).author(this.author).authorLink(this.authorLink)
				.html(this.html).css(this.css).build();
	}
}

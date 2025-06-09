package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theme_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeTagsEntity {
	@EmbeddedId
	private ThemeTagsId id; // PK > theme_tags.id, theme_tags.id

	@MapsId("themeId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theme_id", nullable = false, referencedColumnName = "id")
	private ThemesEntity theme; // FK > themes.id

	@MapsId("tagId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false, referencedColumnName = "id")
	private TagsEntity tag; // FK > tags.id

	@Builder
	public ThemeTagsEntity(ThemesEntity theme, TagsEntity tag) {
		this.id = ThemeTagsId.builder().themeId(theme.getId()).tagId(tag.getId()).build();
		this.theme = theme;
		this.tag = tag;
	}
}

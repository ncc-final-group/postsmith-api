package com.postsmith.api.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeTagsId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "theme_id", nullable = false)
	private Integer themeId; // FK > themes.id

	@Column(name = "tag_id", nullable = false)
	private Integer tagId; // FK > tags.id

	@Builder
	public ThemeTagsId(Integer themeId, Integer tagId) {
		this.themeId = themeId;
		this.tagId = tagId;
	}
}

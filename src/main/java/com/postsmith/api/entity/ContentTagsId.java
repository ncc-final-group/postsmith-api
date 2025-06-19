package com.postsmith.api.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentTagsId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "content_id", nullable = false)
	private Integer contentId; // FK > contents.id

	@Column(name = "tag_id", nullable = false)
	private Integer tagId; // FK > tags.id

	@Builder
	public ContentTagsId(Integer contentId, Integer tagId) {
		this.contentId = contentId;
		this.tagId = tagId;
	}
}

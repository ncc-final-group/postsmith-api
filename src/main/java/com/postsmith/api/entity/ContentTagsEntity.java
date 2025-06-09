package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentTagsEntity {
	@EmbeddedId
	private ContentTagsId id; // PK > content_tags.content_id, content_tags.tag_id

	@MapsId("contentId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false, referencedColumnName = "id")
	private ContentsEntity content; // FK > contents.id

	@MapsId("tagId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false, referencedColumnName = "id")
	private TagsEntity tag; // FK > tags.id

	@Builder
	public ContentTagsEntity(ContentsEntity content, TagsEntity tag) {
		this.id = ContentTagsId.builder().contentId(content.getId()).tagId(tag.getId()).build();
		this.content = content;
		this.tag = tag;
	}
}

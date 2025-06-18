package com.postsmith.api.domain.theme.dto;

import com.postsmith.api.entity.TagsEntity;
import com.postsmith.api.entity.TagsEntity.TagEnum;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagsDto {
	private Integer id;
	private TagEnum type;
	private String name;

	public static TagsDto fromEntity(TagsEntity entity) {
		return TagsDto.builder().id(entity.getId()).name(entity.getName()).type(entity.getType()).build();
	}
}

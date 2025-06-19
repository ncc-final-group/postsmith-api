package com.postsmith.api.domain.contents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class CategoryDto {
	private Integer id;
	private String name;

	@JsonProperty("blog")
	private Integer blogId;

	private Integer parentId;

	private Integer sequence;
	private String description;
	private List<CategoryDto> children = new ArrayList<>();

	public CategoriesEntity toEntity(BlogsEntity blog, CategoriesEntity parent) {
		return CategoriesEntity.builder()
				.name(this.name)
				.description(this.description)
				.sequence(this.sequence)
				.blog(blog)
				.parent(parent)
				.build();
	}
}
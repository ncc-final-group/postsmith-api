package com.postsmith.api.domain.customize.dto;

import com.postsmith.api.entity.MenuEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuDto {
	private int id;
	private String name;
	private String type; // DEFAULT, PAGE, CATEGORY, MANUAL
	private String uri;
	private boolean isBlank;
	private boolean isDefault;

	public static MenuDto fromEntity(MenuEntity entity) {
		return MenuDto.builder().id(entity.getId()).name(entity.getName()).uri(entity.getUri()).type(entity.getType().name()).isBlank(entity.getIsBlank()).isDefault(false) // DB에
																																											// 없는
																																											// 필드니까
																																											// false
																																											// 고정 또는
																																											// 프론트에서
																																											// 설정
				.build();
	}
}

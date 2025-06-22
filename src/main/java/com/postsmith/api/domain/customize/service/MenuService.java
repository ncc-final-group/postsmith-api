package com.postsmith.api.domain.customize.service;

import com.postsmith.api.domain.customize.dto.MenuDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.MenuEntity;
import com.postsmith.api.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
	private final MenuRepository menuRepository;

	@Transactional
	public void replaceAllMenus(List<MenuDto> dtos, BlogsEntity blog) {
		menuRepository.deleteAllByBlog(blog); // 블로그별 삭제

		for (MenuDto dto : dtos) {
			MenuEntity entity = MenuEntity.builder().blog(blog).name(dto.getName()).uri(dto.getUri()).type(MenuEntity.MenuEnum.valueOf(dto.getType())).isBlank(dto.isBlank())
					.build();

			menuRepository.save(entity);
		}
	}

	public List<MenuDto> getMenus(BlogsEntity blog) {
		return menuRepository.findAllByBlog(blog).stream().map(MenuDto::fromEntity).toList();
	}

	public MenuDto addMenu(MenuDto menuDto, BlogsEntity blog) {
		MenuEntity entity = MenuEntity.builder()
				.blog(blog)
				.name(menuDto.getName())
				.uri(menuDto.getUri())
				.type(MenuEntity.MenuEnum.valueOf(menuDto.getType()))
				.isBlank(menuDto.isBlank())
				.build();

		MenuEntity savedEntity = menuRepository.save(entity);
		return MenuDto.fromEntity(savedEntity);
	}
}
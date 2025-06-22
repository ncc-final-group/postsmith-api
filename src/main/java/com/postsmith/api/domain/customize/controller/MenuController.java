package com.postsmith.api.domain.customize.controller;

import com.postsmith.api.domain.contents.dto.CategoryDto;
import com.postsmith.api.domain.customize.dto.MenuDto;
import com.postsmith.api.domain.customize.service.MenuService;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.repository.CategoriesRepository;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.repository.ContentsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;
	private final CategoriesRepository categoriesRepository;
	private final ContentsRepository contentsRepository;

	public MenuController(MenuService menuService, CategoriesRepository categoriesRepository, ContentsRepository contentsRepository) {
		this.menuService = menuService;
		this.categoriesRepository = categoriesRepository;
		this.contentsRepository = contentsRepository;
	}

	BlogsEntity blog = new BlogsEntity(1);


	// Controller
	@GetMapping
	public List<MenuDto> getMenus(@RequestParam("blogId") int blogId) {
		BlogsEntity blog = new BlogsEntity(blogId);
		return menuService.getMenus(blog);
	}

	@PostMapping("/add")
	public ResponseEntity<MenuDto> addMenu(@RequestParam("blogId") int blogId, @RequestBody MenuDto menuDto) {
		BlogsEntity blog = new BlogsEntity(blogId);
		MenuDto savedMenu = menuService.addMenu(menuDto, blog);
		return ResponseEntity.ok(savedMenu);
	}

	@PutMapping
	public ResponseEntity<List<MenuDto>> saveMenus(@RequestParam("blogId") int blogId, @RequestBody List<MenuDto> menus) {
		BlogsEntity blog = new BlogsEntity(blogId);
		menuService.replaceAllMenus(menus, blog);
		List<MenuDto> savedMenus = menuService.getMenus(blog);
		return ResponseEntity.ok(savedMenus);
	}

	@GetMapping("/categories")
	public List<CategoryDto> getCategories(@RequestParam int blogId) {
		return categoriesRepository.findByBlogId(blogId).stream().map(c -> {
			CategoryDto dto = new CategoryDto();
			dto.setId(c.getId());
			dto.setName(c.getName());
			return dto;
		}).collect(Collectors.toList());
	}

	@GetMapping("/pages")
	public List<Map<String, ?>> getPages(@RequestParam Integer blogId) {
		return contentsRepository.findByBlog_IdAndType(blogId, ContentsEntity.ContentEnum.PAGE).stream().map(c -> Map.of("id", c.getId(), "title", c.getTitle()))
				.collect(Collectors.toList());
	}

}

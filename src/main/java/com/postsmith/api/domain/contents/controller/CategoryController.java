package com.postsmith.api.domain.contents.controller;

import com.postsmith.api.domain.contents.dto.CategoryDto;
import com.postsmith.api.domain.contents.service.CategoryService;
import com.postsmith.api.entity.CategoriesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoriesService;

	private CategoryDto entityToDto(CategoriesEntity entity) {
		if (entity == null)
			return null;
		CategoryDto dto = new CategoryDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setSequence(entity.getSequence());
		if (entity.getBlog() != null) {
			dto.setBlogId(entity.getBlog().getId());
		}
		dto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
		return dto;
	}

	@GetMapping("/tree")
	public ResponseEntity<List<CategoryDto>> getCategoryTree() {
		List<CategoryDto> tree = categoriesService.getCategoryTreeForCurrentUser();
		return ResponseEntity.ok(tree);
	}

	// 전체저장
	@PutMapping
	public ResponseEntity<Void> saveAllCategories(@RequestBody List<CategoryDto> updatedCategories) {
		categoriesService.saveAllCategories(updatedCategories);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts/counts")
	public Map<Long, Long> getPostCounts(@RequestParam("blogId") int blogId) {
		return categoriesService.getPostCounts(blogId);
	}

}

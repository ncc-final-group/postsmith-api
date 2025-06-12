package com.postsmith.api.domain.contents.controller;

import com.postsmith.api.domain.contents.dto.CategoryDto;
import com.postsmith.api.domain.contents.service.CategoryService;
import com.postsmith.api.entity.CategoriesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private CategoryDto entityToDto(CategoriesEntity entity) {
        if (entity == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSequence(entity.getSequence());
        if (entity.getBlog() != null) {
            dto.setBlogId(entity.getBlog().getId());
        }
        dto.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
        return dto;
    }

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        CategoriesEntity saved = categoryService.createCategory(dto);
        return ResponseEntity.ok(entityToDto(saved));
    }

    // 로그인 유저 기준 카테고리 트리 조회
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDto>> getCategoryTree() {
        List<CategoryDto> tree = categoryService.getCategoryTreeForCurrentUser();
        return ResponseEntity.ok(tree);
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // 카테고리 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable int id, @RequestBody CategoryDto dto) {
        CategoriesEntity updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(entityToDto(updated));
    }

    // 카테고리 순서 재정렬
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderCategories(@RequestBody List<CategoryDto> ordered) {
        categoryService.reorderCategories(ordered);
        return ResponseEntity.ok().build();
    }

    // 카테고리 이동
    @PutMapping("/{id}/move")
    public ResponseEntity<CategoryDto> moveCategory(@PathVariable int id,
                                                    @RequestParam(required = false) Integer newParentId) {
        CategoriesEntity moved = categoryService.moveCategory(id, newParentId);
        return ResponseEntity.ok(entityToDto(moved));
    }
}

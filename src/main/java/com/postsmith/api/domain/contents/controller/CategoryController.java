package com.postsmith.api.domain.contents.controller;

import com.postsmith.api.domain.contents.dto.CategoryDto;
import com.postsmith.api.domain.contents.service.CategoryService;
import com.postsmith.api.entity.CategoriesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //카테고리 생성
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        log.info("Received category creation request with DTO: {}", dto);
        log.info("Blog ID from DTO: {}", dto.getBlogId());
        CategoriesEntity created = categoryService.createCategory(dto);
        CategoryDto response = new CategoryDto();
        BeanUtils.copyProperties(created, response);
        if (created.getBlog() != null) {
            response.setBlogId(created.getBlog().getId());
        }
        if (created.getCategory() != null) {
            response.setCategoryId(created.getCategory().getId());
        }
        return response;
    }

    // 특정 블로그의 카테고리 트리 조회
    @GetMapping("/tree/{blogId}")
    public ResponseEntity<List<CategoryDto>> getCategoryTreeByBlogId(@PathVariable Integer blogId) {
        List<CategoryDto> categoryTree = categoryService.getCategoryTreeByBlogId(blogId);
        return ResponseEntity.ok(categoryTree);
    }

    //삭제
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }

    //업데이트
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable int id, @RequestBody CategoryDto dto) {
        CategoriesEntity updated = categoryService.updateCategory(id, dto);
        CategoryDto response = new CategoryDto();
        BeanUtils.copyProperties(updated, response);
        if (updated.getBlog() != null) {
            response.setBlogId(updated.getBlog().getId());
        }
        if (updated.getCategory() != null) {
            response.setCategoryId(updated.getCategory().getId());
        }
        return response;
    }

    //카테고리 드래그로 이동시 순서 변경
    @PutMapping("/reorder")
    public void reorderCategories(@RequestBody List<CategoryDto> ordered) {
        categoryService.reorderCategories(ordered);
    }

    //카테고리 메인 <-> 서브
    @PutMapping("/{id}/move")
    public CategoryDto moveCategory(@PathVariable int id, @RequestParam(required = false) Integer newParentId) {
        CategoriesEntity moved = categoryService.moveCategory(id, newParentId);
        CategoryDto response = new CategoryDto();
        BeanUtils.copyProperties(moved, response);
        if (moved.getBlog() != null) {
            response.setBlogId(moved.getBlog().getId());
        }
        if (moved.getCategory() != null) {
            response.setCategoryId(moved.getCategory().getId());
        }
        return response;
    }
}


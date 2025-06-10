package com.postsmith.api.domain.contents.service;

import com.postsmith.api.domain.contents.dto.CategoryDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.CategoriesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoriesRepository categoryRepository;
    private final BlogsRepository blogsRepository;

    @Transactional
    public CategoriesEntity createCategory(CategoryDto dto) {
        // 필수 필드 검증
        if (dto.getBlogId() == null) {
            throw new IllegalArgumentException("Blog ID must not be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be null or empty");
        }

        // 블로그 조회
        BlogsEntity blog = blogsRepository.findById(dto.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + dto.getBlogId()));

        // 상위 카테고리 조회 (있는 경우에만)
        CategoriesEntity parentCategory = null;
        if (dto.getCategoryId() != null) {
            log.info("Looking for parent category with ID: {}", dto.getCategoryId());
            parentCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with ID: " + dto.getCategoryId()));
            log.info("Found parent category: {}", parentCategory.getName());
        }

        // 카테고리 생성
        CategoriesEntity category = CategoriesEntity.builder()
                .blog(blog)
                .name(dto.getName())
                .description(dto.getDescription())
                .sequence(dto.getSequence() != null ? dto.getSequence() : 0)
                .category(parentCategory)
                .build();

        // 저장
        category = categoryRepository.save(category);
        log.info("Created new category with ID: {}", category.getId());
        
        return category;
    }

    public List<CategoryDto> getCategoryTreeByBlogId(Integer blogId) {
        List<CategoriesEntity> all = categoryRepository.findAll().stream()
                .filter(c -> c.getBlog() != null && c.getBlog().getId().equals(blogId))
                .sorted((c1, c2) -> {
                    Integer seq1 = c1.getSequence() != null ? c1.getSequence() : 0;
                    Integer seq2 = c2.getSequence() != null ? c2.getSequence() : 0;
                    return seq1.compareTo(seq2);
                })
                .collect(Collectors.toList());

        // 메인 카테고리만 추출
        List<CategoryDto> result = new ArrayList<>();
        Map<Integer, CategoryDto> mainMap = new HashMap<>();

        for (CategoriesEntity entity : all) {
            if (entity.getCategory() == null) {
                CategoryDto main = convertToDto(entity);
                result.add(main);
                mainMap.put(entity.getId(), main);
            }
        }

        // 서브 카테고리 연결
        for (CategoriesEntity entity : all) {
            if (entity.getCategory() != null) {
                CategoryDto parent = mainMap.get(entity.getCategory().getId());
                if (parent != null) {
                    parent.getChildren().add(convertToDto(entity));
                }
            }
        }

        return result;
    }

    private CategoryDto convertToDto(CategoriesEntity entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (entity.getBlog() != null) {
            dto.setBlogId(entity.getBlog().getId());
        }
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }
        dto.setSequence(entity.getSequence());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    @Transactional
    public void deleteCategory(int id) {
        CategoriesEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 자식 카테고리들 찾기
        List<CategoriesEntity> children = categoryRepository.findAll().stream()
                .filter(c -> c.getCategory() != null && c.getCategory().getId().equals(id))
                .collect(Collectors.toList());

        // 자식 카테고리들의 부모 참조 제거
        for (CategoriesEntity child : children) {
            child.setCategory(null);  // 직접 엔티티 수정
            categoryRepository.save(child);
            log.info("Updated child category {} to remove parent reference", child.getId());
        }

        // 카테고리 삭제
        categoryRepository.deleteById(id);
        log.info("Deleted category {}", id);
    }

    @Transactional
    public CategoriesEntity updateCategory(int id, CategoryDto dto) {
        CategoriesEntity oldCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoriesEntity updatedCategory = CategoriesEntity.builder()
                .id(oldCategory.getId())  // 기존 ID 유지
                .blog(oldCategory.getBlog())
                .category(oldCategory.getCategory())
                .sequence(oldCategory.getSequence())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return categoryRepository.save(updatedCategory);
    }

    @Transactional
    public void reorderCategories(List<CategoryDto> orderedList) {
        Map<Integer, List<CategoryDto>> grouped = orderedList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getCategoryId() == null ? 0 : dto.getCategoryId()));

        for (List<CategoryDto> group : grouped.values()) {
            for (int i = 0; i < group.size(); i++) {
                CategoryDto dto = group.get(i);
                CategoriesEntity oldEntity = categoryRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

                CategoriesEntity updatedEntity = CategoriesEntity.builder()
                        .blog(oldEntity.getBlog())
                        .category(oldEntity.getCategory())
                        .sequence(i)
                        .name(oldEntity.getName())
                        .description(oldEntity.getDescription())
                        .build();

                categoryRepository.save(updatedEntity);
            }
        }
    }

    @Transactional
    public CategoriesEntity moveCategory(int id, Integer newParentId) {
        CategoriesEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoriesEntity parentCategory = null;
        if (newParentId != null) {
            parentCategory = categoryRepository.findById(newParentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }

        // 기존 엔티티 수정
        category.setCategory(parentCategory);
        
        return categoryRepository.save(category);
    }
}

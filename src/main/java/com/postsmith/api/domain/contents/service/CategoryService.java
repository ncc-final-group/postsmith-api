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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoriesRepository categoryRepository;
    private final BlogsRepository blogsRepository;

    //인증 구현 시 현재 로그인 유저의 blogId 가져오는 로직으로 대체 필요
    private Integer getCurrentUserBlogId() {
        return 1; // 테스트용 고정값
    }

    /**
     * 새 카테고리 생성
     */
    @Transactional
    public CategoriesEntity createCategory(CategoryDto dto) {
        if (dto.getBlogId() == null) {
            dto.setBlogId(getCurrentUserBlogId());
        }
        validateCreateDto(dto);

        BlogsEntity blog = blogsRepository.findById(dto.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + dto.getBlogId()));

        CategoriesEntity parentCategory = null;
        if (dto.getCategoryId() != null) {
            parentCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with ID: " + dto.getCategoryId()));
        }

        Integer sequence = dto.getSequence() != null ? dto.getSequence() : 0;

        CategoriesEntity category = CategoriesEntity.builder()
                .blog(blog)
                .category(parentCategory)
                .sequence(sequence)
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return categoryRepository.save(category);
    }

    private void validateCreateDto(CategoryDto dto) {
        if (dto.getBlogId() == null) {
            throw new IllegalArgumentException("Blog ID must not be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be null or empty");
        }
    }

    /**
     * 현재 로그인 유저의 블로그 카테고리 트리 조회 (정렬 포함)
     */
    public List<CategoryDto> getCategoryTreeForCurrentUser() {
        Integer blogId = getCurrentUserBlogId();

        // blogId에 해당하는 카테고리만 필터, 시퀀스 오름차순 정렬
        List<CategoriesEntity> allCategories = categoryRepository.findAll().stream()
                .filter(c -> c.getBlog() != null && blogId.equals(c.getBlog().getId()))
                .sorted(Comparator.comparingInt(c -> c.getSequence() == null ? 0 : c.getSequence()))
                .collect(Collectors.toList());

        return buildCategoryTree(allCategories);
    }

    private List<CategoryDto> buildCategoryTree(List<CategoriesEntity> allCategories) {
        List<CategoryDto> roots = new ArrayList<>();
        Map<Integer, CategoryDto> dtoMap = new HashMap<>();

        // 루트 카테고리 생성
        for (CategoriesEntity entity : allCategories) {
            if (entity.getCategory() == null) {
                CategoryDto rootDto = convertToDto(entity);
                roots.add(rootDto);
                dtoMap.put(entity.getId(), rootDto);
            }
        }

        // 자식 카테고리 연결
        for (CategoriesEntity entity : allCategories) {
            if (entity.getCategory() != null) {
                CategoryDto parentDto = dtoMap.get(entity.getCategory().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(convertToDto(entity));
                }
            }
        }

        return roots;
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

    /**
     * 카테고리 삭제 (자식 카테고리 처리 포함)
     */
    @Transactional
    public void deleteCategory(int id) {
        List<CategoriesEntity> allCategories = categoryRepository.findAll();

        CategoriesEntity category = allCategories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        List<CategoriesEntity> children = findChildCategories(category, allCategories);

        if (!children.isEmpty()) {
            // 자식이 있으면 자식들을 상위 카테고리로 이동시키고 시퀀스 조정
            moveChildrenToParent(children, category);
            updateSiblingSequences(category, children.size(), allCategories);
        } else {
            // 자식 없으면 기존 부모 내 시퀀스 감소 처리
            decrementSiblingSequences(category, allCategories);
        }

        categoryRepository.deleteById(id);
    }

    private List<CategoriesEntity> findChildCategories(CategoriesEntity parent, List<CategoriesEntity> all) {
        return all.stream()
                .filter(c -> c.getCategory() != null && c.getCategory().getId().equals(parent.getId()))
                .collect(Collectors.toList());
    }

    private void moveChildrenToParent(List<CategoriesEntity> children, CategoriesEntity category) {
        int sequenceStart = category.getSequence();
        for (CategoriesEntity child : children) {
            child.changeCategory(category.getCategory());  // 한 단계 위로 이동
            child.changeSequence(sequenceStart++);
        }
        categoryRepository.saveAll(children);
    }

    private void updateSiblingSequences(CategoriesEntity category, int increment, List<CategoriesEntity> allCategories) {
        List<CategoriesEntity> siblings = findLaterSiblings(category, allCategories);
        siblings.forEach(c -> c.changeSequence(c.getSequence() + increment));
        categoryRepository.saveAll(siblings);
    }

    private void decrementSiblingSequences(CategoriesEntity category, List<CategoriesEntity> allCategories) {
        List<CategoriesEntity> siblings = findLaterSiblings(category, allCategories);
        siblings.forEach(c -> c.changeSequence(c.getSequence() - 1));
        categoryRepository.saveAll(siblings);
    }

    private List<CategoriesEntity> findLaterSiblings(CategoriesEntity category, List<CategoriesEntity> allCategories) {
        return allCategories.stream()
                .filter(c -> isSameLevel(c, category) && c.getSequence() > category.getSequence())
                .collect(Collectors.toList());
    }

    private boolean isSameLevel(CategoriesEntity c1, CategoriesEntity c2) {
        if (c1.getCategory() == null && c2.getCategory() == null) return true;
        if (c1.getCategory() != null && c2.getCategory() != null) {
            return c1.getCategory().getId().equals(c2.getCategory().getId());
        }
        return false;
    }

    /**
     * 카테고리 정보 업데이트 (이름, 설명 등)
     */
    @Transactional
    public CategoriesEntity updateCategory(int id, CategoryDto dto) {
        CategoriesEntity existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        // 기존 부모, 시퀀스, 블로그 유지하고 이름, 설명만 변경
        CategoriesEntity updated = CategoriesEntity.builder()
                .id(existing.getId())
                .blog(existing.getBlog())
                .category(existing.getCategory())
                .sequence(existing.getSequence())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return categoryRepository.save(updated);
    }

    /**
     * 카테고리 순서 재조정 (동일 부모 내)
     */
    @Transactional
    public void reorderCategories(List<CategoryDto> orderedList) {
        Map<Integer, List<CategoryDto>> groupedByParent = orderedList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getCategoryId() == null ? 0 : dto.getCategoryId()));

        for (List<CategoryDto> siblings : groupedByParent.values()) {
            for (int i = 0; i < siblings.size(); i++) {
                CategoryDto dto = siblings.get(i);

                CategoriesEntity existing = categoryRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getId()));

                CategoriesEntity updated = CategoriesEntity.builder()
                        .blog(existing.getBlog())
                        .category(existing.getCategory())
                        .sequence(i)
                        .name(existing.getName())
                        .description(existing.getDescription())
                        .build();

                categoryRepository.save(updated);
            }
        }
    }
    //카테고리 이동
    @Transactional
    public CategoriesEntity moveCategory(int dragId, int targetId) {
        List<CategoriesEntity> allCategories = categoryRepository.findAll();

        CategoriesEntity dragCategory = findCategoryById(dragId, allCategories);
        CategoriesEntity targetCategory = findCategoryById(targetId, allCategories);

        if (dragCategory == null || targetCategory == null) {
            throw new RuntimeException("Invalid drag or target category ID");
        }

        // 1. dragCategory의 현재 부모와 시퀀스 기억
        CategoriesEntity oldParent = dragCategory.getCategory();
        int oldSequence = dragCategory.getSequence();

        // 2. dragCategory 자식 포함 전체 이동 대상 수집
        List<CategoriesEntity> subtree = findSubtreeCategories(dragCategory, allCategories);

        // 3. dragCategory 자식 포함 subtree 제거 후 시퀀스 재조정
        removeCategoriesFromParentSequence(subtree, allCategories);

        // 4. targetCategory 부모 동일한 레벨 내 시퀀스 재조정 (target보다 큰 seq +1)
        List<CategoriesEntity> newSiblings = findLaterSiblings(targetCategory, allCategories);
        newSiblings.forEach(c -> c.changeSequence(c.getSequence() + subtree.size()));
        categoryRepository.saveAll(newSiblings);

        // 5. dragCategory를 targetCategory의 자식으로 이동 및 시퀀스 설정
        dragCategory.changeCategory(targetCategory);
        dragCategory.changeSequence(targetCategory.getSequence());
        categoryRepository.save(dragCategory);

        // 6. subtree 자식들은 dragCategory의 하위 카테고리로 유지 (따로 변경 없음)
        // 필요한 경우 하위 카테고리 시퀀스 조정도 추가 가능

        // 7. 전체 저장
        categoryRepository.saveAll(subtree);
        return dragCategory;
    }

    private CategoriesEntity findCategoryById(int id, List<CategoriesEntity> allCategories) {
        return allCategories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private List<CategoriesEntity> findSubtreeCategories(CategoriesEntity root, List<CategoriesEntity> allCategories) {
        List<CategoriesEntity> subtree = new ArrayList<>();
        collectSubtree(root, allCategories, subtree);
        return subtree;
    }

    private void collectSubtree(CategoriesEntity node, List<CategoriesEntity> allCategories, List<CategoriesEntity> collector) {
        collector.add(node);
        List<CategoriesEntity> children = findChildCategories(node, allCategories);
        for (CategoriesEntity child : children) {
            collectSubtree(child, allCategories, collector);
        }
    }

    private void removeCategoriesFromParentSequence(List<CategoriesEntity> subtree, List<CategoriesEntity> allCategories) {
        // subtree 중 최상위 노드 시퀀스 기준으로 동일 레벨에서 시퀀스 - subtree.size() 적용
        CategoriesEntity topNode = subtree.get(0);
        List<CategoriesEntity> siblings = findLaterSiblings(topNode, allCategories);
        siblings.forEach(c -> c.changeSequence(c.getSequence() - subtree.size()));
        categoryRepository.saveAll(siblings);
    }
}

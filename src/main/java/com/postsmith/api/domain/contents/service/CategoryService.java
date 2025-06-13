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

    private List<CategoryDto> buildCategoryTree(List<CategoriesEntity> allEntities) {
        List<CategoryDto> roots = new ArrayList<>();
        Map<Integer, CategoryDto> dtoMap = new HashMap<>();

        // 모든 엔티티 → DTO 변환 및 맵에 저장 (중복 없이 1회)
        for (CategoriesEntity entity : allEntities) {
            CategoryDto dto = convertToDto(entity);
            dtoMap.put(entity.getId(), dto);
        }

        // 트리 구성
        for (CategoriesEntity entity : allEntities) {
            CategoryDto dto = dtoMap.get(entity.getId());
            if (entity.getCategory() == null) {
                // 루트 카테고리
                roots.add(dto);
            } else {
                CategoryDto parentDto = dtoMap.get(entity.getCategory().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }

        return roots;
    }


    private CategoryDto convertToDto(CategoriesEntity entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSequence(entity.getSequence());
        dto.setBlogId(entity.getBlog().getId());
        dto.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
        dto.setChildren(new ArrayList<>());
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

    private boolean isDescendantOf(CategoriesEntity maybeChild, CategoriesEntity maybeParent, List<CategoriesEntity> all) {
        if (maybeChild == null || maybeParent == null) return false;
        CategoriesEntity current = maybeChild.getCategory();
        while (current != null) {
            if (current.getId().equals(maybeParent.getId())) return true;
            current = findCategoryById(current.getId(), all).getCategory();
        }
        return false;
    }


    //카테고리 이동
    @Transactional
    public CategoriesEntity moveCategory(int dragId, int targetId) {
        List<CategoriesEntity> all = categoryRepository.findAll();

        CategoriesEntity drag = findCategoryById(dragId, all);
        CategoriesEntity target = targetId == 0 ? null : findCategoryById(targetId, all);

        if (drag == null) throw new RuntimeException("Drag 대상 없음");
        if (targetId != 0 && target == null) throw new RuntimeException("Target 대상 없음");

        // 🔒 순환 참조 방지
        if (isDescendantOf(target, drag, all)) {
            throw new RuntimeException("자기 자식 카테고리 아래로 이동 불가");
        }

        // 이동할 전체 서브트리 수집
        List<CategoriesEntity> subtree = findSubtreeCategories(drag, all);

        // 기존 위치에서 제거 → 시퀀스 감소
        removeCategoriesFromParentSequence(subtree, all);

        // 새 위치 설정
        if (target == null) {
            // 루트로 승격
            drag.changeCategory(null);
            int maxSeq = all.stream()
                    .filter(c -> c.getCategory() == null)
                    .mapToInt(c -> c.getSequence() == null ? 0 : c.getSequence())
                    .max()
                    .orElse(-1);
            drag.changeSequence(maxSeq + 1);
        } else {
            // 일반 이동 (target의 부모로 이동)
            CategoriesEntity newParent = target.getCategory();
            drag.changeCategory(newParent);
            drag.changeSequence(target.getSequence());

            // 기존 형제들 시퀀스 +1
            List<CategoriesEntity> newSiblings = findLaterSiblings(target, all);
            newSiblings.forEach(s -> s.changeSequence(s.getSequence() + subtree.size()));
            categoryRepository.saveAll(newSiblings);
        }

        categoryRepository.saveAll(subtree);
        return drag;
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

    @Transactional
    public void saveAllCategories(List<CategoryDto> updatedCategories) {
        Map<Integer, CategoriesEntity> entityMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(CategoriesEntity::getId, c -> c));

        // 블로그는 1개라고 가정
        BlogsEntity blog = blogsRepository.findById(updatedCategories.get(0).getBlogId())
                .orElseThrow(() -> new RuntimeException("블로그가 존재하지 않습니다"));

        List<CategoriesEntity> entitiesToSave = new ArrayList<>();

        for (CategoryDto dto : updatedCategories) {
            CategoriesEntity entity = entityMap.get(dto.getId());

            if (entity != null) {
                // ✅ 기존 엔티티 직접 수정
                entity.changeName(dto.getName());
                entity.changeDescription(dto.getDescription());
                entity.changeSequence(dto.getSequence());

                CategoriesEntity parent = dto.getCategoryId() == null ? null : entityMap.get(dto.getCategoryId());
                entity.changeCategory(parent);

                entitiesToSave.add(entity);
            } else {
                // ✅ 신규 카테고리 생성 (builder 사용)
                CategoriesEntity newCategory = CategoriesEntity.builder()
                        .blog(blog)
                        .category(dto.getCategoryId() == null ? null : entityMap.get(dto.getCategoryId()))
                        .sequence(dto.getSequence())
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .build();

                entitiesToSave.add(newCategory);
            }
        }

        categoryRepository.saveAll(entitiesToSave);
    }


}

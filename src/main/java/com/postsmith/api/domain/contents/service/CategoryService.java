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

    private final BlogsRepository blogsRepository;
    private final CategoriesRepository categoriesRepository;

    // 테스트용: 현재 로그인 사용자 블로그 아이디
    private Integer getCurrentUserBlogId() {return 1;}


    private CategoryDto convertToDto(CategoriesEntity entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSequence(entity.getSequence());
        dto.setBlogId(entity.getBlog().getId());
        dto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
        dto.setChildren(new ArrayList<>());
        return dto;
    }



    // 카테고리 트리 조회
    public List<CategoryDto> getCategoryTreeForCurrentUser() {
        Integer blogId = getCurrentUserBlogId();
        List<CategoriesEntity> allCategories = categoriesRepository.findAll().stream()
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

        // 루트 카테고리와 서브카테고리 분리
        List<CategoryDto> rootCategories = new ArrayList<>();
        List<CategoryDto> subCategories = new ArrayList<>();

        for (CategoriesEntity entity : allEntities) {
            CategoryDto dto = dtoMap.get(entity.getId());
            if (entity.getParent() == null) {
                rootCategories.add(dto);
            } else {
                subCategories.add(dto);
            }
        }

        // 서브카테고리를 부모에 연결
        for (CategoryDto subDto : subCategories) {
            CategoryDto parentDto = dtoMap.get(subDto.getParentId());
            if (parentDto != null) {
                parentDto.getChildren().add(subDto);
            }
        }

        return rootCategories;
    }

    // 전체 카테고리 상태 저장 (추가/수정/삭제 모두 처리)
    @Transactional
    public void saveAllCategories(List<CategoryDto> updatedCategories) {
        if (updatedCategories.isEmpty()) {
            categoriesRepository.deleteAll();
            return;
        }

        BlogsEntity blog = blogsRepository.findById(updatedCategories.get(0).getBlogId())
                .orElseThrow(() -> new RuntimeException("블로그가 존재하지 않습니다"));

        // 기존 DB 데이터 불러오기
        Map<Integer, CategoriesEntity> existingMap = categoriesRepository.findAll().stream()
                .collect(Collectors.toMap(CategoriesEntity::getId, c -> c));

        // 삭제할 것 찾기
        Set<Integer> updatedIds = updatedCategories.stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toSet());

        List<CategoriesEntity> toDelete = existingMap.values().stream()
                .filter(e -> !updatedIds.contains(e.getId()))
                .collect(Collectors.toList());
        categoriesRepository.deleteAll(toDelete);

        // 임시 ID → Entity 매핑
        Map<Integer, CategoriesEntity> tempMap = new HashMap<>();
        List<CategoriesEntity> toSave = new ArrayList<>();

        // 1단계: Entity 생성 (parent 없이)
        for (CategoryDto dto : updatedCategories) {
            CategoriesEntity entity;
            boolean isNew = dto.getId() < 0;

            if (!isNew && existingMap.containsKey(dto.getId())) {
                entity = existingMap.get(dto.getId());
                entity.changeName(dto.getName());
                entity.changeDescription(dto.getDescription());
                entity.changeSequence(dto.getSequence());
            } else {
                entity = CategoriesEntity.builder()
                        .blog(blog)
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .sequence(dto.getSequence())
                        .build();
            }

            tempMap.put(dto.getId(), entity);
            toSave.add(entity);
        }

        // 먼저 저장해서 실제 DB ID 발급받기
        categoriesRepository.saveAll(toSave);

        // 2단계: 부모 설정
        for (CategoryDto dto : updatedCategories) {
            CategoriesEntity entity = tempMap.get(dto.getId());
            CategoriesEntity parent = dto.getParentId() == null ? null : tempMap.get(dto.getParentId());
            entity.changeCategory(parent);
        }

        // 부모 설정 후 재저장
        categoriesRepository.saveAll(toSave);

        categoriesRepository.findAll().forEach(e -> {
            log.info("저장된 카테고리: id={}, name={}, seq={}, parentId={}",
                    e.getId(), e.getName(), e.getSequence(),
                    e.getParent() != null ? e.getParent().getId() : null);
        });
    }


}
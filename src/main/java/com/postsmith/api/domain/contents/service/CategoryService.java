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
	private Integer getCurrentUserBlogId() {
		return 1;
	}

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
		List<CategoriesEntity> allCategories = categoriesRepository.findAll().stream().filter(c -> c.getBlog() != null && blogId.equals(c.getBlog().getId()))
				.sorted(Comparator.comparingInt(c -> c.getSequence() == null ? 0 : c.getSequence())).collect(Collectors.toList());

		return buildCategoryTree(allCategories);
	}

	private List<CategoryDto> buildCategoryTree(List<CategoriesEntity> allEntities) {
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

		Integer blogId = updatedCategories.stream()
				.map(CategoryDto::getBlogId)
				.filter(Objects::nonNull)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("모든 카테고리에서 blogId가 누락되었습니다."));

		BlogsEntity blog = blogsRepository.findById(blogId)
				.orElseThrow(() -> new RuntimeException("블로그가 존재하지 않습니다."));

		// 기존 DB 데이터 불러오기
		Map<Integer, CategoriesEntity> existingMap = categoriesRepository.findAll().stream()
				.collect(Collectors.toMap(CategoriesEntity::getId, c -> c));

		// 삭제 대상 계산
		Set<Integer> updatedIds = updatedCategories.stream()
				.map(CategoryDto::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		List<CategoriesEntity> toDelete = existingMap.values().stream()
				.filter(entity -> !updatedIds.contains(entity.getId()))
				.toList();
		categoriesRepository.deleteAll(toDelete);

		// ID 기준 DTO 맵
		Map<Integer, CategoryDto> dtoMap = updatedCategories.stream()
				.filter(dto -> dto.getId() != null)
				.collect(Collectors.toMap(CategoryDto::getId, dto -> dto));

		// 1단계: Entity 생성 (parent 없이)
		Map<Integer, CategoriesEntity> entityMap = new HashMap<>();
		List<CategoriesEntity> toSave = new ArrayList<>();

		for (CategoryDto dto : updatedCategories) {
			CategoriesEntity entity;

			boolean isNew = dto.getId() == null || !existingMap.containsKey(dto.getId());
			if (isNew) {
				entity = dto.toEntity(blog, null); // parent는 이후 설정
			} else {
				entity = existingMap.get(dto.getId());
				entity.updateCategory(blog, null, dto.getName(), dto.getSequence(), dto.getDescription()); // parent 제외
			}

			entityMap.put(dto.getId(), entity);
			toSave.add(entity);
		}

		categoriesRepository.saveAll(toSave); // 먼저 parent 없이 저장

		// 2단계: 부모 설정
		for (CategoryDto dto : updatedCategories) {
			CategoriesEntity entity = entityMap.get(dto.getId());

			CategoriesEntity parent = null;
			Integer parentId = dto.getParentId();
			if (parentId != null) {
				parent = entityMap.get(parentId);
				if (parent == null) parent = existingMap.get(parentId); // 기존 DB에 있던 부모
			}

			entity.updateCategory(blog, parent, dto.getName(), dto.getSequence(), dto.getDescription());
		}

		categoriesRepository.saveAll(toSave); // 최종 저장
	}


	public Map<Long, Long> getPostCounts(int blogId) {
		List<Object[]> raw = categoriesRepository.countPostsByCategoryId(blogId);
		return raw.stream().collect(Collectors.toMap(row -> ((Number) row[0]).longValue(), // category_id
				row -> ((Number) row[1]).longValue() // count
		));
	}

}
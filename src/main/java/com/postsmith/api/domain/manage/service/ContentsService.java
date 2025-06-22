package com.postsmith.api.domain.manage.service;

import com.postsmith.api.domain.manage.dto.ContentsRequestDto;
import com.postsmith.api.domain.manage.dto.ContentsResponseDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.repository.ContentsRepository;
import com.postsmith.api.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentsService {
	private final ContentsRepository contentsRepository;
	private final CategoriesRepository categoriesRepository;

	@Transactional
	public ContentsResponseDto createPost(BlogsEntity blog, ContentsRequestDto dto) {
		try {
			// 카테고리 조회 (nullable)
			CategoriesEntity category = null;
			if (dto.getCategory() != null) {
				category = categoriesRepository.findById(dto.getCategory()).orElse(null);
			}

			// HTML 태그 제거하여 plain text 생성
			String plainContent = dto.getContent() != null ? dto.getContent().replaceAll("<[^>]*>", "") : "";

			// 다음 시퀀스 번호 생성
			Integer nextSequence = contentsRepository.findLatestSequenceByBlog(blog) + 1;

			// PostType을 ContentEnum으로 변환
			ContentsEntity.ContentEnum contentType;
			try {
				contentType = ContentsEntity.ContentEnum.valueOf(dto.getPostType().toUpperCase());
			} catch (IllegalArgumentException e) {
				log.warn("Invalid post type: {}, using POSTS as default", dto.getPostType());
				contentType = ContentsEntity.ContentEnum.POSTS;
			}

			// ContentsEntity 생성
			ContentsEntity content = ContentsEntity.builder().blog(blog).category(category).sequence(nextSequence).type(contentType).title(dto.getTitle())
					.contentHtml(dto.getContent()).contentPlain(plainContent).thumbnail(dto.getThumbnail()).isTemp(dto.getIsTemp()).isPublic(dto.getIsPublic()).likes(0).build();

			// 생성 시간 설정 (Entity의 @PrePersist가 없다면 수동 설정)
			ContentsResponseDto savedContent = contentsRepository.save(content).toDto();

			log.info("Content created successfully: blogId={}, sequence={}, title={}", blog.getId(), nextSequence, dto.getTitle());

			return savedContent;

		} catch (Exception e) {
			log.error("Error creating content: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to create content: " + e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	public List<ContentsResponseDto> getBlogContents(BlogsEntity blog) {
		return contentsRepository.findByBlogOrderBySequenceDesc(blog).stream().map(ContentsEntity::toDto).toList();
	}

	@Transactional(readOnly = true)
	public List<ContentsResponseDto> getPublicBlogContents(BlogsEntity blog) {
		return contentsRepository.findByBlogAndIsPublicTrueOrderBySequenceDesc(blog).stream().map(ContentsEntity::toDto).toList();
	}

	@Transactional(readOnly = true)
	public ContentsResponseDto getContentBySequence(BlogsEntity blog, Integer sequence) {
		return contentsRepository.findByBlogAndSequence(blog, sequence)
				.orElseThrow(() -> new IllegalArgumentException("Content not found: blogId=" + blog.getId() + ", sequence=" + sequence)).toDto();
	}

	@Transactional(readOnly = true)
	public ContentsEntity getContentById(Integer id) {
		return contentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + id));
	}

	@Transactional
	public ContentsResponseDto updateContent(Integer contentId, ContentsRequestDto dto) {
		ContentsEntity content = getContentById(contentId);

		// 카테고리 조회 및 업데이트 (nullable)
		if (dto.getCategory() != null) {
			CategoriesEntity category = categoriesRepository.findById(dto.getCategory()).orElse(null);
			content.updateCategory(category);
		}

		// HTML 태그 제거하여 plain text 생성
		String plainContent = dto.getContent() != null ? dto.getContent().replaceAll("<[^>]*>", "") : "";

		// PostType을 ContentEnum으로 변환 및 업데이트
		if (dto.getPostType() != null) {
			try {
				ContentsEntity.ContentEnum contentType = ContentsEntity.ContentEnum.valueOf(dto.getPostType().toUpperCase());
				content.updateType(contentType);
			} catch (IllegalArgumentException e) {
				log.warn("Invalid post type: {}, keeping existing type", dto.getPostType());
			}
		}

		// 콘텐츠 내용 업데이트
		content.updateContent(dto.getTitle() != null ? dto.getTitle() : content.getTitle(), dto.getContent() != null ? dto.getContent() : content.getContentHtml(), plainContent,
				dto.getThumbnail() != null ? dto.getThumbnail() : content.getThumbnail(), dto.getIsTemp() != null ? dto.getIsTemp() : content.getIsTemp(), dto.getIsPublic() != null ? dto.getIsPublic() : content.getIsPublic());

		ContentsEntity savedContent = contentsRepository.save(content);

		log.info("Content updated successfully: id={}, title={}", contentId, dto.getTitle());

		return savedContent.toDto();
	}

	@Transactional
	public void deleteContent(Integer contentId) {
		ContentsEntity content = getContentById(contentId);
		contentsRepository.delete(content);
		log.info("Content deleted successfully: id={}", contentId);
	}

	@Transactional(readOnly = true)
	public List<ContentsEntity> searchContents(BlogsEntity blog, String keyword) {
		return contentsRepository.findByBlogAndTitleContainingIgnoreCaseOrderBySequenceDesc(blog, keyword);
	}

	@Transactional(readOnly = true)
	public List<ContentsResponseDto> getDraftContents(BlogsEntity blog, String type) {
		List<ContentsEntity> draftContents;

		if (type != null && !type.isEmpty()) {
			// 특정 타입의 임시저장 콘텐츠만 조회
			try {
				ContentsEntity.ContentEnum contentType = ContentsEntity.ContentEnum.valueOf(type.toUpperCase());
				draftContents = contentsRepository.findByBlogAndIsTempTrueAndTypeOrderBySequenceDesc(blog, contentType);
			} catch (IllegalArgumentException e) {
				log.warn("Invalid content type: {}, returning all drafts", type);
				draftContents = contentsRepository.findByBlogAndIsTempTrueOrderBySequenceDesc(blog);
			}
		} else {
			// 모든 타입의 임시저장 콘텐츠 조회
			draftContents = contentsRepository.findByBlogAndIsTempTrueOrderBySequenceDesc(blog);
		}

		return draftContents.stream().map(ContentsEntity::toDto).toList();
	}


}

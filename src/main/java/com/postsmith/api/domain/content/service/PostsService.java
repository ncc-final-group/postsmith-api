package com.postsmith.api.domain.content.service;

import com.postsmith.api.domain.content.dto.PostsDto;

import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.ContentViewsRepository;
import com.postsmith.api.repository.ContentsRepository;
import com.postsmith.api.repository.RepliesRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class PostsService {

	private final ContentsRepository contentRepository;
	private final ContentViewsRepository contentViewsRepository;
	private final RepliesRepository repliesRepository;
	private final BlogsRepository blogsRepository;

	public PostsService(ContentsRepository contentRepository, ContentViewsRepository contentViewsRepository, RepliesRepository repliesRepository, BlogsRepository blogsRepository) {
		this.contentRepository = contentRepository;
		this.contentViewsRepository = contentViewsRepository;
		this.repliesRepository = repliesRepository;
		this.blogsRepository =blogsRepository;
	}

	public List<PostsDto> getPostsByTypeAndBlogId(ContentsEntity.ContentEnum type, Long blogId) {
		List<ContentsEntity> contents = contentRepository.findByTypeAndBlogId(type, blogId);

		return contents.stream().map(content -> {
			PostsDto dto = new PostsDto();
			dto.setBlogId(content.getBlog().getId());
			dto.setContentId(content.getId());
			dto.setUserNickname(content.getBlog().getUser().getNickname());
			dto.setContentType(content.getType());
			dto.setTitle(content.getTitle());
			dto.setIsPublic(content.getIsPublic());
			dto.setLikes(content.getLikes());
			dto.setCreatedAt(content.getCreatedAt());
			dto.setSequence(content.getSequence());
			dto.setAddress(content.getBlog().getAddress());

			CategoriesEntity category = content.getCategory();
			if (category != null) {
				dto.setCategoryid(category.getId());
				dto.setCategoryName(category.getName());

				List<String> pathParts = new ArrayList<>();
				CategoriesEntity current = category;
				while (current != null) {
					pathParts.add(current.getName());
					current = current.getParent();
				}
				Collections.reverse(pathParts);
				dto.setCategoryPath(String.join("/", pathParts));
			}

			int totalViews = contentViewsRepository.findTotalViewsByContentId(content.getId());
			int totalReplies = repliesRepository.findTotalRepliesByContentId(content.getId());
			dto.setTotalViewCount(totalViews);
			dto.setTotalRepliesCount(totalReplies);

			return dto;
		}).collect(Collectors.toList());
	}

	public void deletePostById(Integer contentId) {
		ContentsEntity content = contentRepository.findById(contentId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 콘텐츠가 존재하지 않습니다: " + contentId));
		contentRepository.delete(content);
	}

	public void deletePostsByIds(List<Integer> ids) {
		for (Integer id : ids) {
			deletePostById(id); // 기존 단일 삭제 메서드 재사용
		}
	}

	public void updatePrivacy(Integer id, boolean isPublic) {
		contentRepository.updateIsPublicById(id, isPublic);
	}

	public void updateIsPublicForContents(List<Integer> contentIds, Boolean isPublic) {
		contentRepository.updateIsPublicByIds(contentIds, isPublic);
	}

	public Integer findBlogIdByAddress(String address) {
	    return blogsRepository.findIdByAddress(address)
	        .orElseThrow(() -> new IllegalArgumentException("주소에 해당하는 블로그가 없습니다: " + address));
	}





}

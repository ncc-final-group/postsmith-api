package com.postsmith.api.domain.feedContent.service;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.domain.feedContent.dto.FeedContentsDto;
import com.postsmith.api.repository.ContentsRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedContentsService {

	private final ContentsRepository contentsRepository;

	// 피드 컨텐츠(구독중인 블로글의 게시글) 찾기
	public List<FeedContentsDto> findFeedContents(Integer userId) {
		List<Object[]> results = contentsRepository.findFeedContents(userId);

		return results.stream().map((Object[] row) -> FeedContentsDto.fromEntity((ContentsEntity) row[0], (BlogsEntity) row[1])).collect(Collectors.toList());
	}
}

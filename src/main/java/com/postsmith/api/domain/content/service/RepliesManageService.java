package com.postsmith.api.domain.content.service;

import com.postsmith.api.domain.content.dto.RepliesManageDto;
import com.postsmith.api.domain.content.dto.RepliesManagePostDto;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.RepliesEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.ContentsRepository;
import com.postsmith.api.repository.RepliesRepository;
import com.postsmith.api.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepliesManageService {

	private final RepliesRepository repliesRepository;
	private final UsersRepository usersRepository;
	private final ContentsRepository contentsRepository;

	public RepliesManageService(RepliesRepository repliesRepository, UsersRepository usersRepository, ContentsRepository contentsRepository) {
		this.repliesRepository = repliesRepository;
		this.usersRepository = usersRepository;
		this.contentsRepository = contentsRepository;

	}

	public List<RepliesManageDto> getRepliesByBlogId(Long blogId) {
		List<RepliesEntity> replies = repliesRepository.findByContentBlogId(blogId);

		return replies.stream().map(reply -> {
			RepliesManageDto dto = new RepliesManageDto();
			dto.setRepliesId(reply.getId());
			dto.setUserName(reply.getUser() != null ? reply.getUser().getNickname() : "탈퇴한 사용자");
			dto.setParentReplyId(reply.getParentReply() != null ? reply.getParentReply().getId() : null);
			dto.setReplyContent(reply.getContentText());
			dto.setContentTitle(reply.getContent().getTitle());
			dto.setContentId(reply.getContent().getId());
			dto.setCreatedAt(reply.getCreatedAt());
			dto.setSequence(reply.getContent().getSequence());
			dto.setAddress(reply.getContent().getBlog().getAddress());
			
			return dto;
		}).collect(Collectors.toList());
	}

	public void saveReply(RepliesManagePostDto dto) {
		// 유효성 검증
		UsersEntity user = usersRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		ContentsEntity content = contentsRepository.findById(dto.getContentId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));

		RepliesEntity reply = new RepliesEntity();
		reply.setUser(user); // UsersEntity 객체 설정
		reply.setContent(content); // ContentsEntity 객체 설정
		reply.setContentText(dto.getContentText()); // 댓글 본문
		reply.setCreatedAt(LocalDateTime.now());

		if (dto.getReplyId() != null) {
			RepliesEntity parentReply = repliesRepository.findById(dto.getReplyId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글입니다."));
			reply.setParentReply(parentReply);
		}

		repliesRepository.save(reply);
	}

	public void deleteReplyById(Integer replyId) {
		RepliesEntity reply = repliesRepository.findById(replyId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 콘텐츠가 존재하지 않습니다: " + replyId));
		repliesRepository.delete(reply);
	}

	public void deleteRepliesByIds(List<Integer> ids) {
		for (Integer id : ids) {
			deleteReplyById(id); // 기존 단일 삭제 메서드 재사용
		}
	}

}

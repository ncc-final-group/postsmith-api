package com.postsmith.api.content.service;

import com.postsmith.api.content.dto.RepliesManageDto;
import com.postsmith.api.entity.RepliesEntity;
import com.postsmith.api.repository.RepliesRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepliesManageService {

    private final RepliesRepository repliesRepository;
    
    public RepliesManageService(RepliesRepository repliesRepository)
    {this.repliesRepository = repliesRepository;}
    
    
    public List<RepliesManageDto> getReplies() {
        List<RepliesEntity> replies = repliesRepository.findAll(); // ← 커스텀 쿼리 필요

        return replies.stream().map(reply -> {
            RepliesManageDto dto = new RepliesManageDto();
            dto.setRepliesId(reply.getId());
            dto.setUserName(reply.getUser() != null ? reply.getUser().getNickname() : "탈퇴한 사용자");
            dto.setParentReplyId(reply.getParentReply() != null ? reply.getParentReply().getId() : null);
            dto.setReplyContent(reply.getContentText());
            dto.setContentTitle(reply.getContent().getTitle()); // 댓글이 달린 게시글 제목
            dto.setCreatedAt(reply.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteReplyById(Integer replyId) {
        RepliesEntity reply = repliesRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 콘텐츠가 존재하지 않습니다: " + replyId));
        repliesRepository.delete(reply);
    }

    public void deleteRepliesByIds(List<Integer> ids) {
        for (Integer id : ids) {
            deleteReplyById(id);  // 기존 단일 삭제 메서드 재사용
        }
    }

}

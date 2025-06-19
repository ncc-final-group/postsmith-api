package com.postsmith.api.domain.replies.service;

import com.postsmith.api.domain.replies.dto.RepliesCreateDto;
import com.postsmith.api.domain.replies.dto.RepliesDto;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.RepliesEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.ContentsRepository;
import com.postsmith.api.repository.RepliesRepository;
import com.postsmith.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RepliesService {
    
    private final RepliesRepository repliesRepository;
    private final UsersRepository usersRepository;
    private final ContentsRepository contentsRepository;

    /**
     * 댓글 생성
     */
    public RepliesDto createReply(RepliesCreateDto createDto) {
        // 사용자 조회
        UsersEntity user = usersRepository.findById(createDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + createDto.getUserId()));

        // 콘텐츠 조회
        ContentsEntity content = contentsRepository.findById(createDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다: " + createDto.getContentId()));

        // 부모 댓글 조회 (대댓글인 경우)
        RepliesEntity parentReply = null;
        if (createDto.getParentReplyId() != null) {
            parentReply = repliesRepository.findById(createDto.getParentReplyId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다: " + createDto.getParentReplyId()));
        }

        // 댓글 엔티티 생성
        RepliesEntity reply = RepliesEntity.builder()
                .user(user)
                .content(content)
                .parentReply(parentReply)
                .contentText(createDto.getContentText())
                .build();

        // 생성 시간 설정 (Entity에 @PrePersist가 없다면 수동 설정)
        setCreatedAt(reply, LocalDateTime.now());

        // 댓글 저장
        RepliesEntity savedReply = repliesRepository.save(reply);
        
        return RepliesDto.fromEntity(savedReply);
    }

    /**
     * 콘텐츠별 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<RepliesDto> getRepliesByContentId(Integer contentId) {
        ContentsEntity content = contentsRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다: " + contentId));

        List<RepliesEntity> replies = repliesRepository.findByContentAndDeletedAtIsNullOrderByCreatedAtAsc(content);
        
        return replies.stream()
                .map(RepliesDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제 (soft delete)
     */
    public void deleteReply(Integer replyId, Integer userId) {
        RepliesEntity reply = repliesRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + replyId));

        // 작성자 확인
        if (!reply.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        // soft delete 처리
        setDeletedAt(reply, LocalDateTime.now());
        repliesRepository.save(reply);
    }

    // Helper method for setting createdAt via reflection
    private void setCreatedAt(RepliesEntity reply, LocalDateTime createdAt) {
        try {
            java.lang.reflect.Field field = RepliesEntity.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(reply, createdAt);
        } catch (Exception e) {
            System.out.println("Failed to set createdAt: " + e.getMessage());
        }
    }

    // Helper method for setting deletedAt via reflection
    private void setDeletedAt(RepliesEntity reply, LocalDateTime deletedAt) {
        try {
            java.lang.reflect.Field field = RepliesEntity.class.getDeclaredField("deletedAt");
            field.setAccessible(true);
            field.set(reply, deletedAt);
        } catch (Exception e) {
            System.out.println("Failed to set deletedAt: " + e.getMessage());
        }
    }
}

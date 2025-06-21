package com.postsmith.api.domain.manage.service;

import com.postsmith.api.domain.manage.dto.ContentViewsDto;
import com.postsmith.api.domain.manage.dto.ContentVisitsDto;
import com.postsmith.api.entity.ContentViewsEntity;
import com.postsmith.api.entity.ContentVisitsEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.ContentViewsRepository;
import com.postsmith.api.repository.ContentVisitsRepository;
import com.postsmith.api.repository.ContentsRepository;
import com.postsmith.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentStatsService {
    private final ContentViewsRepository contentViewsRepository;
    private final ContentVisitsRepository contentVisitsRepository;
    private final ContentsRepository contentsRepository;
    private final UsersRepository usersRepository;
    @Transactional
    public ContentViewsDto recordView(ContentViewsDto contentViewsDto) {
        Optional<ContentsEntity> opContent = contentsRepository.findById(contentViewsDto.getContentId());
        if(opContent.isPresent()) {
            try {
                // UPSERT 방식으로 조회수 기록/증가
                contentViewsRepository.upsertViewCount(contentViewsDto.getContentId());
                
                // 업데이트된 결과를 조회해서 반환
                ContentsEntity content = opContent.get();
                LocalDate today = LocalDate.now();
                Optional<ContentViewsEntity> entity = contentViewsRepository.findByContentAndCreatedOn(content, today);
                
                if (entity.isPresent()) {
                    return entity.get().toDto();
                } else {
                    // 만약 조회되지 않는다면 기본값 반환
                    return ContentViewsDto.builder()
                            .contentId(contentViewsDto.getContentId())
                            .viewsCount(1)
                            .createdOn(today)
                            .build();
                }
            } catch (Exception e) {
                log.error("Failed to record view for contentId: {}", contentViewsDto.getContentId(), e);
                throw new RuntimeException("Failed to record content view", e);
            }
        } else {
            throw new IllegalArgumentException("Content with id " + contentViewsDto.getContentId() + " does not exist.");
        }
    }

    public ContentVisitsDto recordVisit(ContentVisitsDto dto) {
        Optional<ContentsEntity> opContent = contentsRepository.findById(dto.getContentId());
        if(opContent.isPresent()) {
            // Content 가 존재하면
            ContentsEntity content = opContent.get();
            // DTO 에서 사용자 ID를 가져와서 User 엔티티를 조회, 없으면 null로 설정
            UsersEntity user = null;
            if (dto.getUserId() != null) {
                user = usersRepository.findById(dto.getUserId()).orElse(null);
            }
            
            // 중복 체크: 같은 IP와 오늘 날짜로 이미 방문 기록이 있는지 확인
            Optional<ContentVisitsEntity> existingEntity = contentVisitsRepository.findByIpAndCreatedOn(dto.getIpAddress(), LocalDate.now());
            
            if(existingEntity.isEmpty()) {
                // 오늘 처음 방문하는 경우 새 레코드 생성
                ContentVisitsEntity newEntity = ContentVisitsEntity.builder()
                        .content(content)
                        .user(user)  // user는 null이거나 실제 사용자 엔티티
                        .ip(dto.getIpAddress())
                        .build();
                return contentVisitsRepository.save(newEntity).toDto();
            } else {
                // 이미 방문 기록이 있는 경우
                ContentVisitsEntity entity = existingEntity.get();
                // 사용자 정보가 있고 기존 레코드에 사용자 정보가 없으면 업데이트
                if (user != null && entity.getUser() == null) {
                    entity.setUser(user);
                    return contentVisitsRepository.save(entity).toDto();
                }
                // 이미 방문한 기록이므로 기존 데이터 반환
                return entity.toDto();
            }
        } else {
            throw new IllegalArgumentException("Content with id " + dto.getContentId() + " does not exist.");
        }
    }

    public Integer getTotalViewsByContentId(Integer contentId) {
        Optional<ContentsEntity> opContent = contentsRepository.findById(contentId);
        if(opContent.isPresent()) {
            ContentsEntity content = opContent.get();
            ContentViewsEntity contentViewsEntity = contentViewsRepository.findByContentAndCreatedOn(content, LocalDate.now())
                    .orElse(null);
            log.info("Total views for content ID {}: {}", contentId, contentViewsEntity != null ? contentViewsEntity.getViewsCount() : 0);
            return contentViewsEntity != null ? contentViewsEntity.getViewsCount() : 0;
        } else {
            throw new IllegalArgumentException("Content with id " + contentId + " does not exist.");
        }
    }
}

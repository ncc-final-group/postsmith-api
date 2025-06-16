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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentStatsService {
    private final ContentViewsRepository contentViewsRepository;
    private final ContentVisitsRepository contentVisitsRepository;
    private final ContentsRepository contentsRepository;
    private final UsersRepository usersRepository;

    public ContentViewsDto recordView(ContentViewsDto contentViewsDto) {
        Optional<ContentsEntity> opContent = contentsRepository.findById(contentViewsDto.getContentId());
        if(opContent.isPresent()) {
            ContentsEntity content = opContent.get();
            LocalDate today = LocalDate.now();
            ContentViewsEntity entity = contentViewsRepository.findByContentAndCreatedOn(content, today)
                    .orElse(ContentViewsEntity.builder().content(content)
                            .viewsCount(0)
                            .createdOn(today)
                            .build());
            entity.incrementViewsCount();
            return contentViewsRepository.save(entity).toDto();
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
            UsersEntity user;
            if (dto.getUserId() != null) {
                user = usersRepository.findById(dto.getUserId()).orElse(null);
            } else {
                user = null;
            }
            // 오늘 날짜에 해당하는 방문 기록을 조회 ( 사용자 기반 또는 IP 기반 )
            ContentVisitsEntity entity = contentVisitsRepository.findByUserAndCreatedOn(user, LocalDate.now())
                    .orElseGet(() -> contentVisitsRepository.findByIpAndCreatedOn(dto.getIpAddress(), LocalDate.now())
                            // IP 와 생성 날짜로 조회 후 user 가 없으면 user 설정
                            .map(e -> {
                                if(e.getUser() == null){
                                    e.setUser(user); // IP 기반 방문 기록에 사용자 설정
                                }
                                return e;
                            })
                            // 방문이 없으면 새로 생성
                    .orElse(ContentVisitsEntity.builder().content(content).user(user).ip(dto.getIpAddress())
                            .build()));
            return contentVisitsRepository.save(entity).toDto();
        } else {
            throw new IllegalArgumentException("Content with id " + dto.getContentId() + " does not exist.");
        }
    }

    public Integer getTotalViewsByContentId(Integer contentId) {
        Optional<ContentsEntity> opContent = contentsRepository.findById(contentId);
        if(opContent.isPresent()) {
            ContentsEntity content = opContent.get();
            return contentViewsRepository.getTotalViewsByContent(content);
        } else {
            throw new IllegalArgumentException("Content with id " + contentId + " does not exist.");
        }
    }
}

package com.postsmith.api.domain.content.service;

import com.postsmith.api.domain.content.dto.MainContentsDto;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainContentsService {
    private final ContentsRepository contentsRepository;
    private final BlogsRepository blogsRepository;

    private MainContentsDto toMainContentsDto(ContentsEntity entity) {
        MainContentsDto dto = new MainContentsDto();
        dto.setId(entity.getId());
        dto.setBlogId(entity.getBlog() != null ? entity.getBlog().getId() : null);
        dto.setBlogAddress(entity.getBlog() != null ? entity.getBlog().getAddress() : null);
        dto.setSequence(entity.getSequence());
        dto.setContentHtml(entity.getContentHtml());
        dto.setTitle(entity.getTitle());
        dto.setContentPlain(entity.getContentPlain());
        dto.setThumbnail(entity.getThumbnail()); // 썸네일 추가
        dto.setLikes(entity.getLikes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }




    public List<MainContentsDto> getTop3RecommendedContents() {
        return contentsRepository.findTop3ByLikes(PageRequest.of(0, 3))
                .stream()
                .map(this::toMainContentsDto)
                .toList();
    }

    public List<MainContentsDto> getRandomRecentContents() {
        List<ContentsEntity> top3 = contentsRepository.findTop3ByLikes(PageRequest.of(0, 3));
        List<Integer> excludeIds = top3.stream()
                .map(ContentsEntity::getId)
                .toList();

        List<ContentsEntity> combined = new ArrayList<>();

        // 1. 먼저 추천글 제외하고 랜덤 뽑기
        List<ContentsEntity> filtered = contentsRepository.findRandom7PublicContentsExcluding(excludeIds);
        combined.addAll(filtered);

        // 2. 만약 7개보다 적으면 추천글 포함 랜덤으로 부족한 개수만큼 추가
        if (combined.size() < 7) {
            List<ContentsEntity> fallback = contentsRepository.findRandom7PublicContents();
            for (ContentsEntity c : fallback) {
                if (combined.size() >= 7) break;
                if (!excludeIds.contains(c.getId()) && !combined.contains(c)) {
                    combined.add(c);
                }
            }
        }

        System.out.println("추천글 ID: " + excludeIds);
        System.out.println("최종 랜덤 최신글 ID: " + combined.stream().map(ContentsEntity::getId).toList());

        return combined.stream()
                .map(this::toMainContentsDto)
                .toList();
    }

    public String getBlogAddressByUserId(Integer userId) {
        return blogsRepository.findTopAddressByUserId(userId);
    }


}

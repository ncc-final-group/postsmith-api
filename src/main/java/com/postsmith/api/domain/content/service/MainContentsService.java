package com.postsmith.api.domain.content.service;

import com.postsmith.api.domain.content.dto.MainContentsDto;
import com.postsmith.api.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainContentsService {
    private final ContentsRepository contentsRepository;

    public List<MainContentsDto> getTop3RecommendedContents() {
        return contentsRepository.findTop3ByLikes(PageRequest.of(0, 3))
                .stream()
                .map(MainContentsDto::MainContentsDtofromEntity)
                .toList();
    }

    public List<MainContentsDto> getRandomRecentContents() {
        return contentsRepository.findRandom7PublicContents()
                .stream()
                .map(MainContentsDto::MainContentsDtofromEntity)
                .toList();
    }
}

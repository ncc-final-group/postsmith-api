package com.postsmith.api.theme.service;

import com.postsmith.api.theme.dto.TagsDto;
import com.postsmith.api.repository.TagsRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsService {
private final TagsRepository tagsRepository;
    
    public List<TagsDto> getAllTags() {
    	return tagsRepository.findAll()
    			.stream()
    			.map(TagsDto::fromEntity)
    			.collect(Collectors.toList());
    }
    
}

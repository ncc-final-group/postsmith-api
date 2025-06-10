package com.example.postsmith_api.service.media;

import com.example.postsmith_api.domain.media.MediaFile;
import com.example.postsmith_api.dto.media.MediaFileDto;
import com.example.postsmith_api.repository.media.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;

    // 미디어 파일 저장
    public MediaFileDto saveMediaFile(MediaFileDto mediaFileDto) {
        MediaFile mediaFile = mediaFileDto.toEntity();
        MediaFile savedFile = mediaFileRepository.save(mediaFile);
        log.info("미디어 파일 저장됨: {}", savedFile.getOriginalFileName());
        return MediaFileDto.fromEntity(savedFile);
    }

    // 사용자별 미디어 파일 목록 조회
    @Transactional(readOnly = true)
    public Page<MediaFileDto> getUserMediaFiles(Integer userId, Pageable pageable) {
        Page<MediaFile> mediaFiles = mediaFileRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return mediaFiles.map(MediaFileDto::fromEntity);
    }

    // 파일 타입별 조회
    @Transactional(readOnly = true)
    public Page<MediaFileDto> getUserMediaFilesByType(Integer userId, String fileType, Pageable pageable) {
        Page<MediaFile> mediaFiles = mediaFileRepository.findByUserIdAndFileTypeOrderByCreatedAtDesc(userId, fileType, pageable);
        return mediaFiles.map(MediaFileDto::fromEntity);
    }

    // 키워드 검색
    @Transactional(readOnly = true)
    public Page<MediaFileDto> searchMediaFiles(Integer userId, String keyword, Pageable pageable) {
        Page<MediaFile> mediaFiles = mediaFileRepository.searchByKeyword(userId, keyword, pageable);
        return mediaFiles.map(MediaFileDto::fromEntity);
    }

    // 미디어 파일 상세 조회
    @Transactional(readOnly = true)
    public MediaFileDto getMediaFile(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("미디어 파일을 찾을 수 없습니다: " + id));
        return MediaFileDto.fromEntity(mediaFile);
    }

    // 미디어 파일 수정
    public MediaFileDto updateMediaFile(Long id, MediaFileDto updateDto) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("미디어 파일을 찾을 수 없습니다: " + id));

        // 수정 가능한 필드들만 업데이트
        if (updateDto.getAltText() != null) {
            mediaFile.setAltText(updateDto.getAltText());
        }
        if (updateDto.getDescription() != null) {
            mediaFile.setDescription(updateDto.getDescription());
        }
        if (updateDto.getBlogId() != null) {
            mediaFile.setBlogId(updateDto.getBlogId());
        }

        MediaFile updatedFile = mediaFileRepository.save(mediaFile);
        log.info("미디어 파일 수정됨: {}", updatedFile.getOriginalFileName());
        return MediaFileDto.fromEntity(updatedFile);
    }

    // 미디어 파일 삭제
    public void deleteMediaFile(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("미디어 파일을 찾을 수 없습니다: " + id));

        mediaFileRepository.delete(mediaFile);
        log.info("미디어 파일 삭제됨: {}", mediaFile.getOriginalFileName());
    }

    // 여러 파일 삭제
    public void deleteMediaFiles(List<Long> ids) {
        List<MediaFile> mediaFiles = mediaFileRepository.findAllById(ids);
        mediaFileRepository.deleteAll(mediaFiles);
        log.info("미디어 파일 {} 개 삭제됨", mediaFiles.size());
    }

    // 사용자 미디어 통계 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getUserMediaStats(Integer userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 총 파일 수
        long totalCount = mediaFileRepository.findByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged()).getTotalElements();
        stats.put("totalCount", totalCount);
        
        // 총 파일 크기
        Long totalSize = mediaFileRepository.getTotalFileSizeByUserId(userId);
        stats.put("totalSize", totalSize);
        
        // 파일 타입별 개수
        List<Object[]> typeCountsList = mediaFileRepository.getFileTypeCountsByUserId(userId);
        Map<String, Long> typeCounts = new HashMap<>();
        for (Object[] typeCount : typeCountsList) {
            typeCounts.put((String) typeCount[0], (Long) typeCount[1]);
        }
        stats.put("typeCounts", typeCounts);
        
        return stats;
    }

    // URL로 중복 확인
    @Transactional(readOnly = true)
    public boolean isFileUrlExists(String fileUrl) {
        return mediaFileRepository.existsByFileUrl(fileUrl);
    }
} 
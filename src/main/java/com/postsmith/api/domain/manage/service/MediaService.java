
package com.postsmith.api.domain.manage.service;

import com.postsmith.api.domain.blog.BlogService;
import com.postsmith.api.domain.manage.dto.MediaDto;
import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.MediaEntity;
import com.postsmith.api.repository.MediaRepository;
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
public class MediaService {

    private final MediaRepository mediaRepository;
    private final BlogService blogService;

    @Transactional
    public MediaDto saveMedia(MediaDto mediaDto) {
        try {
            log.info("MediaService.saveMedia 호출됨: {}", mediaDto);

            // 블로그 조회
            log.info("블로그 조회 시작: blogId={}", mediaDto.getBlogId());
            BlogsEntity blog = blogService.findBlogById(mediaDto.getBlogId());
            log.info("블로그 조회 완료: blogId={}, blogName={}", blog.getId(), blog.getName());

            // URI 중복 확인
            log.info("URI 중복 확인: {}", mediaDto.getUri());
            if (mediaRepository.existsByUri(mediaDto.getUri())) {
                log.warn("URI 중복 발견: {}", mediaDto.getUri());
                throw new IllegalArgumentException("URI already exists: " + mediaDto.getUri());
            }

            // MediaEntity 생성 및 저장
            log.info("MediaEntity 생성 시작");
            MediaEntity media = mediaDto.toEntity(blog);
            log.info("MediaEntity 생성 완료: {}", media);

            log.info("MediaEntity 저장 시작");
            MediaEntity savedMedia = mediaRepository.save(media);
            log.info("MediaEntity 저장 완료: id={}", savedMedia.getId());

            log.info("Media saved successfully: id={}, filename={}",
                    savedMedia.getId(), savedMedia.getName());

            return MediaDto.fromEntity(savedMedia);

        } catch (Exception e) {
            log.error("Error saving media: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save media: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Page<MediaDto> getBlogMediaFiles(Integer blogId, Pageable pageable) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            Page<MediaEntity> mediaEntities = mediaRepository.findByBlogOrderByCreatedAtDesc(blog, pageable);
            return mediaEntities.map(MediaDto::fromEntity);
        } catch (Exception e) {
            log.error("Error getting blog media files: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get blog media files: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Page<MediaDto> getBlogMediaFilesByType(Integer blogId, String fileType, Pageable pageable) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            Page<MediaEntity> mediaEntities = mediaRepository.findByBlogAndTypeOrderByCreatedAtDesc(blog, fileType, pageable);
            return mediaEntities.map(MediaDto::fromEntity);
        } catch (Exception e) {
            log.error("Error getting blog media files by type: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get blog media files by type: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Page<MediaDto> searchMediaFiles(Integer blogId, String keyword, Pageable pageable) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            Page<MediaEntity> mediaEntities = mediaRepository.searchByBlogAndKeyword(blog, keyword, pageable);
            return mediaEntities.map(MediaDto::fromEntity);
        } catch (Exception e) {
            log.error("Error searching media files: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search media files: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public MediaDto getMediaFile(Integer id) {
        try {
            MediaEntity media = mediaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Media not found with id: " + id));
            return MediaDto.fromEntity(media);
        } catch (Exception e) {
            log.error("Error getting media file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get media file: " + e.getMessage(), e);
        }
    }

    @Transactional
    public MediaDto updateMediaFile(Integer id, MediaDto updateDto) {
        try {
            MediaEntity media = mediaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Media not found with id: " + id));

            // 수정 가능한 필드들만 업데이트
            media.updateMediaInfo(updateDto.getFilename(), updateDto.getFileType());

            MediaEntity updatedMedia = mediaRepository.save(media);
            log.info("Media updated successfully: id={}, filename={}",
                    updatedMedia.getId(), updatedMedia.getName());

            return MediaDto.fromEntity(updatedMedia);

        } catch (Exception e) {
            log.error("Error updating media file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update media file: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteMediaFile(Integer id) {
        try {
            MediaEntity media = mediaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Media not found with id: " + id));

            mediaRepository.delete(media);
            log.info("Media deleted successfully: id={}, filename={}",
                    media.getId(), media.getName());

        } catch (Exception e) {
            log.error("Error deleting media file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete media file: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteMediaFiles(List<Integer> ids) {
        try {
            List<MediaEntity> mediaEntities = mediaRepository.findAllById(ids);
            mediaRepository.deleteAll(mediaEntities);
            log.info("Deleted {} media files", mediaEntities.size());
        } catch (Exception e) {
            log.error("Error deleting media files: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete media files: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getBlogMediaStats(Integer blogId) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            Map<String, Object> stats = new HashMap<>();

            // 총 파일 수
            Long totalCount = mediaRepository.countByBlog(blog);
            stats.put("totalCount", totalCount);

            // 총 파일 크기
            Long totalSize = mediaRepository.getTotalFileSizeByBlog(blog);
            stats.put("totalSize", totalSize);

            // 파일 타입별 개수
            List<Object[]> typeCountsList = mediaRepository.getFileTypeCountsByBlog(blog);
            Map<String, Long> typeCounts = new HashMap<>();
            for (Object[] typeCount : typeCountsList) {
                typeCounts.put((String) typeCount[0], (Long) typeCount[1]);
            }
            stats.put("typeCounts", typeCounts);

            return stats;

        } catch (Exception e) {
            log.error("Error getting blog media stats: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get blog media stats: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public boolean isUriExists(String uri) {
        return mediaRepository.existsByUri(uri);
    }

    @Transactional(readOnly = true)
    public Page<MediaDto> getBlogMediaFilesByDateRange(Integer blogId, LocalDateTime startDate,
                                                       LocalDateTime endDate, Pageable pageable) {
        try {
            BlogsEntity blog = blogService.findBlogById(blogId);
            Page<MediaEntity> mediaEntities = mediaRepository.findByBlogAndCreatedAtBetweenOrderByCreatedAtDesc(
                    blog, startDate, endDate, pageable);
            return mediaEntities.map(MediaDto::fromEntity);
        } catch (Exception e) {
            log.error("Error getting blog media files by date range: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get blog media files by date range: " + e.getMessage(), e);
        }
    }
}

package com.example.postsmith_api.repository.media;

import com.example.postsmith_api.domain.media.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    
    // 사용자별 미디어 파일 조회
    Page<MediaFile> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);
    
    // 블로그별 미디어 파일 조회
    Page<MediaFile> findByBlogIdOrderByCreatedAtDesc(Integer blogId, Pageable pageable);
    
    // 파일 타입별 조회
    Page<MediaFile> findByUserIdAndFileTypeOrderByCreatedAtDesc(Integer userId, String fileType, Pageable pageable);
    
    // 파일명으로 검색
    @Query("SELECT m FROM MediaFile m WHERE m.userId = :userId AND " +
           "(LOWER(m.originalFileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.altText) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY m.createdAt DESC")
    Page<MediaFile> searchByKeyword(@Param("userId") Integer userId, 
                                   @Param("keyword") String keyword, 
                                   Pageable pageable);
    
    // 날짜 범위로 조회
    Page<MediaFile> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Integer userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // 파일 URL로 조회 (중복 확인용)
    boolean existsByFileUrl(String fileUrl);
    
    // 사용자의 총 파일 크기 계산
    @Query("SELECT COALESCE(SUM(m.fileSize), 0) FROM MediaFile m WHERE m.userId = :userId")
    Long getTotalFileSizeByUserId(@Param("userId") Integer userId);
    
    // 파일 타입별 개수 조회
    @Query("SELECT m.fileType, COUNT(m) FROM MediaFile m WHERE m.userId = :userId GROUP BY m.fileType")
    List<Object[]> getFileTypeCountsByUserId(@Param("userId") Integer userId);
} 
package com.postsmith.api.repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.MediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Integer> {

	// 블로그별 미디어 파일 조회
	Page<MediaEntity> findByBlogOrderByCreatedAtDesc(BlogsEntity blog, Pageable pageable);

	// 블로그와 파일 타입별 조회
	Page<MediaEntity> findByBlogAndTypeOrderByCreatedAtDesc(BlogsEntity blog, String type, Pageable pageable);

	// 블로그별 파일명 검색
	@Query("SELECT m FROM MediaEntity m WHERE m.blog = :blog AND " + "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " + "ORDER BY m.createdAt DESC")
	Page<MediaEntity> searchByBlogAndKeyword(@Param("blog") BlogsEntity blog, @Param("keyword") String keyword, Pageable pageable);

	// URI로 중복 확인
	boolean existsByUri(String uri);

	// 블로그별 총 파일 크기 계산
	@Query("SELECT COALESCE(SUM(m.size), 0) FROM MediaEntity m WHERE m.blog = :blog")
	Long getTotalFileSizeByBlog(@Param("blog") BlogsEntity blog);

	// 블로그별 파일 타입별 개수 조회
	@Query("SELECT m.type, COUNT(m) FROM MediaEntity m WHERE m.blog = :blog GROUP BY m.type")
	List<Object[]> getFileTypeCountsByBlog(@Param("blog") BlogsEntity blog);

	// 블로그별 파일 개수 조회
	Long countByBlog(BlogsEntity blog);

	// 날짜 범위로 조회
	Page<MediaEntity> findByBlogAndCreatedAtBetweenOrderByCreatedAtDesc(BlogsEntity blog, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

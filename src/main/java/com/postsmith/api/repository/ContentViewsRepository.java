package com.postsmith.api.repository;

import com.postsmith.api.domain.stats.dto.ViewDto;
import com.postsmith.api.entity.ContentViewsEntity;
import com.postsmith.api.entity.ContentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentViewsRepository extends JpaRepository<ContentViewsEntity, Integer> {
    Optional<ContentViewsEntity> findByContentAndCreatedOn(ContentsEntity content, LocalDate createdOn);

    @Query("SELECT COALESCE(SUM(cv.viewsCount), 0) FROM ContentViewsEntity cv WHERE cv.content = :content")
    Integer getTotalViewsByContent(@Param("content") ContentsEntity content);
	@Query("SELECT COALESCE(SUM(cv.viewsCount), 0) FROM ContentViewsEntity cv WHERE cv.content.id = :contentId")
	int findTotalViewsByContentId(@Param("contentId") Integer contentId);

	@Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId AND cv.createdOn = :createdOn")
	Integer viewsByBlogIdAndCreatedOn(@Param("blogId") Integer blogId, @Param("createdOn") LocalDate createdOn);

	@Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId")
	Integer totalViewsByBlogId(@Param("blogId") Integer blogId);

	@Query("SELECT new com.postsmith.api.domain.stats.dto.ViewDto(b.id, SUM(cv.viewsCount), cv.createdOn) " + "FROM ContentViewsEntity cv " + "JOIN cv.content c "
			+ "JOIN c.blog b " + "WHERE b.id = :blogId " + "GROUP BY cv.createdOn, b.id " + "ORDER BY cv.createdOn")
	List<ViewDto> findViewsByBlogIdGroupByDate(@Param("blogId") Integer blogId);

	@Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv WHERE cv.content.id = :contentId AND cv.createdOn = :createdOn")
	Integer viewsByContentIdAndCreatedOn(@Param("contentId") Integer contentId, @Param("createdOn") LocalDate createdOn);

	@Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv WHERE cv.content.id = :contentId")
	Integer totalViewsByContentId(@Param("contentId") Integer contentId);

	@Query("SELECT new com.postsmith.api.domain.stats.dto.ViewDto(c.id, SUM(cv.viewsCount), cv.createdOn) " + "FROM ContentViewsEntity cv " + "JOIN cv.content c "
			+ "WHERE c.id = :contentId " + "GROUP BY cv.createdOn, c.id " + "ORDER BY cv.createdOn")
	List<ViewDto> findViewsByContentIdGroupByDate(@Param("contentId") Integer contentId);

    // UPSERT 방식으로 조회수 증가 (MySQL의 ON DUPLICATE KEY UPDATE 사용)
    @Modifying
    @Query(value = "INSERT INTO content_views (content_id, views_count, created_on) " +
                   "VALUES (:contentId, 1, CURDATE()) " +
                   "ON DUPLICATE KEY UPDATE views_count = views_count + 1", 
           nativeQuery = true)
    void upsertViewCount(@Param("contentId") Integer contentId);

}
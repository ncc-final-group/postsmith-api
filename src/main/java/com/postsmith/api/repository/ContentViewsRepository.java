package com.postsmith.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentViewsEntity;
import com.postsmith.api.stats.dto.ViewDto;

@Repository
public interface ContentViewsRepository extends JpaRepository<ContentViewsEntity, Integer> {
	 @Query("SELECT COALESCE(SUM(cv.viewsCount), 0) FROM ContentViewsEntity cv WHERE cv.content.id = :contentId")
	    int findTotalViewsByContentId(@Param("contentId") Integer contentId);

	 
	 @Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId AND cv.createdOn = :createdOn")
	 Integer viewsByBlogIdAndCreatedOn(@Param("blogId") Integer blogId, @Param("createdOn") LocalDate createdOn);

	 @Query("SELECT SUM(cv.viewsCount) FROM ContentViewsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId")
	 Integer totalViewsByBlogId(@Param("blogId") Integer blogId);
	 
	 @Query("SELECT new com.postsmith.api.stats.dto.ViewDto(b.id, SUM(cv.viewsCount), cv.createdOn) " +
	        "FROM ContentViewsEntity cv " +
	        "JOIN cv.content c " +
	        "JOIN c.blog b " +
	        "WHERE b.id = :blogId " +
	        "GROUP BY cv.createdOn, b.id " +
	        "ORDER BY cv.createdOn")
	 List<ViewDto> findViewsByBlogIdGroupByDate(@Param("blogId") Integer blogId);

	 
}
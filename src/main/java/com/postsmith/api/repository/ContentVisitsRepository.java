package com.postsmith.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentVisitsEntity;
import com.postsmith.api.stats.dto.VisitDto;

@Repository
public interface ContentVisitsRepository extends JpaRepository<ContentVisitsEntity, Integer> {

	 @Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId AND DATE(cv.createdAt) = :createdOn")
	 Integer countVisitorsByBlogIdAndCreatedOn(@Param("blogId") Integer blogId, @Param("createdOn") LocalDate createdOn);

	 @Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId")
	 Integer countTotalVisitorsByBlogId(@Param("blogId") Integer blogId);

	 @Query("SELECT new com.postsmith.api.stats.dto.VisitDto(b.id, COUNT(DISTINCT cv.ip), DATE(cv.createdAt)) " +
		       "FROM ContentVisitsEntity cv " +
		       "JOIN cv.content c " +
		       "JOIN c.blog b " +
		       "WHERE b.id = :blogId " +
		       "GROUP BY DATE(cv.createdAt), b.id " +
		       "ORDER BY DATE(cv.createdAt)")
		List<VisitDto> findVisitorsByBlogIdGroupByDate(@Param("blogId") Integer blogId);


}

package com.postsmith.api.repository;

import com.postsmith.api.domain.stats.dto.VisitDto;
import com.postsmith.api.entity.ContentVisitsEntity;
import com.postsmith.api.entity.UsersEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentVisitsRepository extends JpaRepository<ContentVisitsEntity, Integer> {
    Optional<ContentVisitsEntity> findByUser(UsersEntity user);
    @Query("SELECT cv FROM ContentVisitsEntity cv WHERE cv.user = :user AND DATE(cv.createdAt) = :createdOn")
    Optional<ContentVisitsEntity> findByUserAndCreatedOn(@Param("user") UsersEntity user, @Param("createdOn") LocalDate createdOn);
    @Query("SELECT cv FROM ContentVisitsEntity cv WHERE cv.ip = :ipAddress AND DATE(cv.createdAt) = :now")
    Optional<ContentVisitsEntity> findByIpAndCreatedOn(@Param("user") String ipAddress, @Param("now") LocalDate now);

	@Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId AND DATE(cv.createdAt) = :createdOn")
	Integer countVisitorsByBlogIdAndCreatedOn(@Param("blogId") Integer blogId, @Param("createdOn") LocalDate createdOn);

	@Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv JOIN cv.content c JOIN c.blog b WHERE b.id = :blogId")
	Integer countTotalVisitorsByBlogId(@Param("blogId") Integer blogId);

	@Query("SELECT new com.postsmith.api.domain.stats.dto.VisitDto(b.id, COUNT(DISTINCT cv.ip), DATE(cv.createdAt)) " + "FROM ContentVisitsEntity cv " + "JOIN cv.content c "
			+ "JOIN c.blog b " + "WHERE b.id = :blogId " + "GROUP BY DATE(cv.createdAt), b.id " + "ORDER BY DATE(cv.createdAt)")
	List<VisitDto> findVisitorsByBlogIdGroupByDate(@Param("blogId") Integer blogId);

	@Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv WHERE cv.content.id = :contentId AND DATE(cv.createdAt) = :createdOn")
	Integer countVisitorsByContentIdAndCreatedOn(@Param("contentId") Integer contentId, @Param("createdOn") LocalDate createdOn);

	@Query("SELECT COUNT(DISTINCT cv.ip) FROM ContentVisitsEntity cv WHERE cv.content.id = :contentId")
	Integer countTotalVisitorsByContentId(@Param("contentId") Integer contentId);

	@Query("SELECT new com.postsmith.api.domain.stats.dto.VisitDto(c.id, COUNT(DISTINCT cv.ip), DATE(cv.createdAt)) " + "FROM ContentVisitsEntity cv " + "JOIN cv.content c "
			+ "WHERE c.id = :contentId " + "GROUP BY DATE(cv.createdAt), c.id " + "ORDER BY DATE(cv.createdAt)")
	List<VisitDto> findVisitorsByContentIdGroupByDate(@Param("contentId") Integer contentId);

}

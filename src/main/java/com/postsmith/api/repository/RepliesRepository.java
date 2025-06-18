package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.RepliesEntity;
import com.postsmith.api.entity.ContentsEntity;
import com.postsmith.api.entity.UsersEntity;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepliesRepository extends JpaRepository<RepliesEntity, Integer> {
	// 콘텐츠별 댓글 조회 (삭제되지 않은 것만)
	List<RepliesEntity> findByContentAndDeletedAtIsNullOrderByCreatedAtAsc(ContentsEntity content);

	// 부모 댓글별 대댓글 조회 (삭제되지 않은 것만)
	List<RepliesEntity> findByParentReplyAndDeletedAtIsNullOrderByCreatedAtAsc(RepliesEntity parentReply);

//	// 사용자별 댓글 조회
//	List<RepliesEntity> findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(UsersEntity user);

	// 콘텐츠별 댓글 수 조회 (삭제되지 않은 것만)
	Long countByContentAndDeletedAtIsNull(ContentsEntity content);

	// 특정 기간 내 작성된 댓글 조회
	List<RepliesEntity> findByContentAndCreatedAtBetweenAndDeletedAtIsNullOrderByCreatedAtDesc(ContentsEntity content, LocalDateTime startDate, LocalDateTime endDate);

	List<RepliesEntity> findByContentBlogId(Long blogId);

	@Query("SELECT COUNT(r) FROM RepliesEntity r WHERE r.content.id = :contentId")
	int findTotalRepliesByContentId(@Param("contentId") Integer contentId);
}

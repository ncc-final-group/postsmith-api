package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.ContentsEntity;

import java.util.Optional;

import jakarta.transaction.Transactional;

@Repository
public interface ContentsRepository extends JpaRepository<ContentsEntity, Integer> {
	List<ContentsEntity> findByBlog_IdAndType(Integer blogId, ContentsEntity.ContentEnum type);

	// 블로그별 콘텐츠 조회
	List<ContentsEntity> findByBlogOrderBySequenceDesc(BlogsEntity blog);

	// 블로그의 최근 시퀀스 번호 조회
	@Query("SELECT COALESCE(MAX(c.sequence), 0) FROM ContentsEntity c WHERE c.blog = :blog")
	Integer findLatestSequenceByBlog(@Param("blog") BlogsEntity blog);

	// 블로그와 시퀀스로 콘텐츠 조회
	Optional<ContentsEntity> findByBlogAndSequence(BlogsEntity blog, Integer sequence);

	// 공개된 콘텐츠만 조회
	List<ContentsEntity> findByBlogAndIsPublicTrueOrderBySequenceDesc(BlogsEntity blog);

	// 제목으로 검색
	List<ContentsEntity> findByBlogAndTitleContainingIgnoreCaseOrderBySequenceDesc(BlogsEntity blog, String title);

	// 타입별 콘텐츠 조회
	List<ContentsEntity> findByBlogAndTypeOrderBySequenceDesc(BlogsEntity blog, ContentsEntity.ContentEnum type);

	// 임시저장 콘텐츠 조회
	List<ContentsEntity> findByBlogAndIsTempTrueOrderBySequenceDesc(BlogsEntity blog);

	// 특정 타입의 임시저장 콘텐츠 조회
	List<ContentsEntity> findByBlogAndIsTempTrueAndTypeOrderBySequenceDesc(BlogsEntity blog, ContentsEntity.ContentEnum type);

	// 좋아요 순으로 콘텐츠 조회
	List<ContentsEntity> findByBlogAndIsPublicTrueOrderByLikesDesc(BlogsEntity blog);

	List<ContentsEntity> findByTypeAndBlogId(ContentsEntity.ContentEnum type, Long blogId);

	@Modifying
	@Transactional
	@Query("UPDATE ContentsEntity c SET c.isPublic = :isPublic WHERE c.id = :id")
	void updateIsPublicById(@Param("id") Integer id, @Param("isPublic") Boolean isPublic);

	@Modifying
	@Transactional
	@Query("UPDATE ContentsEntity c SET c.isPublic = :isPublic WHERE c.id IN :contentIds")
	void updateIsPublicByIds(@Param("contentIds") List<Integer> contentIds, @Param("isPublic") Boolean isPublic);

	// 피드 컨텐츠(구독중인 블로글의 게시글) 찾기
	@Query("""
			    SELECT c, b
			    FROM ContentsEntity c
			    JOIN SubscriptionEntity s ON c.blog.id = s.blog.id
			    JOIN BlogsEntity b ON c.blog.id = b.id
			    WHERE s.subscriber.id = :userId
			    ORDER BY c.createdAt DESC
			""")
	List<Object[]> findFeedContents(@Param("userId") Integer userId);

	// 좋아요 높은 상위 3개 (공개글만)
	@Query("SELECT c FROM ContentsEntity c " +
			"JOIN FETCH c.blog b " +
			"LEFT JOIN FETCH c.category cat " +
			"WHERE c.isPublic = true AND c.type='POSTS' " +
			"ORDER BY c.likes DESC")
	List<ContentsEntity> findTop3ByLikes(Pageable pageable);

	@Query(value = "SELECT * FROM contents c " +
			"WHERE c.is_public = true AND c.type='POSTS' " +
			"ORDER BY RAND() LIMIT 7", nativeQuery = true)
	List<ContentsEntity> findRandom7PublicContents();

	@Query(value = """
    SELECT * FROM contents 
    WHERE is_public = true AND type='POSTS'
    AND id NOT IN (:excludeIds) 
    ORDER BY RAND() 
    LIMIT 7
    """, nativeQuery = true)
	List<ContentsEntity> findRandom7PublicContentsExcluding(@Param("excludeIds") List<Integer> excludeIds);
}

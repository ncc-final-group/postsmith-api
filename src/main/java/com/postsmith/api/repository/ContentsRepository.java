package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.ContentsEntity;

@Repository
public interface ContentsRepository extends JpaRepository<ContentsEntity, Integer> {
	
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
}

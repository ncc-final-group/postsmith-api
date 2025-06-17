package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogsRepository extends JpaRepository<BlogsEntity, Integer> {
	
    boolean existsByAddress(String address);
    Optional<BlogsEntity> findByAddress(String address);
    List<BlogsEntity> findByUserOrderByCreatedAtDesc(UsersEntity user);
    Long countByUser(UsersEntity user);

    // userId로 운영중인 블로그 정보
    List<BlogsEntity> findAllByUser_Id(Integer userId);
    
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE blogs 
        SET name = :name, 
            nickname = :nickname, 
            description = :description 
        WHERE id = :id
        """, nativeQuery = true)
    int updateBlog(@Param("id") Integer id,
                       @Param("name") String name,
                       @Param("nickname") String nickname,
                       @Param("description") String description);
    
    // blog_id로 블로그 정보
    @Query(value = """
    	    SELECT * FROM blogs b
    	    WHERE b.user_id != :userId
    	    AND b.id NOT IN (
    	        SELECT s.blog_id FROM subscription s
    	        WHERE s.subscriber_id = :userId
    	    )
    	    ORDER BY RAND()
    	    LIMIT 8
    	""", nativeQuery = true)
    	List<BlogsEntity> findrecommendedBlogs(@Param("userId") Integer userId);

    // 구독
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO subscription (subscriber_id, blog_id)
        VALUES (:subscriberId, :blogId)
    """, nativeQuery = true)
    int insertSubscription(@Param("subscriberId") Integer subscriberId, @Param("blogId") Integer blogId);

}

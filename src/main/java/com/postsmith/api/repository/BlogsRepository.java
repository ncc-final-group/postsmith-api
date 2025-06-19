package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Repository
public interface BlogsRepository extends JpaRepository<BlogsEntity, Integer> {

	boolean existsByAddress(String address);

	Optional<BlogsEntity> findByAddress(String address);

	@Query("SELECT b.id FROM BlogsEntity b WHERE b.address = :address")
	Optional<Integer> findIdByAddress(@Param("address") String address);

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
			    description = :description,
			    logo_image = :logoImage
			WHERE id = :id
			""", nativeQuery = true)
	int updateBlog(@Param("id") Integer id, @Param("name") String name, @Param("nickname") String nickname, @Param("description") String description,
			@Param("logoImage") String logo_image);

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

	//kjh가 추가
	@Query("""
    SELECT b.address 
    FROM BlogsEntity b 
    WHERE b.user.id = :userId 
    ORDER BY b.createdAt ASC
    LIMIT 1
""")
	String findTopAddressByUserId(@Param("userId") Integer userId);

}

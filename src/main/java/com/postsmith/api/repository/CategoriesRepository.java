package com.postsmith.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postsmith.api.entity.CategoriesEntity;
import com.postsmith.api.entity.BlogsEntity;
import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {

	List<CategoriesEntity> findByBlogId(int blogId);

	// 블로그별 카테고리 조회 (순서대로)
	List<CategoriesEntity> findByBlogOrderBySequenceAsc(BlogsEntity blog);

	// 부모 카테고리별 하위 카테고리 조회 (순서대로)
	List<CategoriesEntity> findByParentOrderBySequenceAsc(CategoriesEntity parent);

	// 최상위 카테고리 조회 (부모가 없는 것들)
	List<CategoriesEntity> findByBlogAndParentIsNullOrderBySequenceAsc(BlogsEntity blog);

	// 블로그의 다음 시퀀스 번호 조회
	@Query("SELECT COALESCE(MAX(c.sequence), 0) + 1 FROM CategoriesEntity c WHERE c.blog = :blog")
	Integer findNextSequenceByBlog(@Param("blog") BlogsEntity blog);

	// 카테고리 이름으로 검색
	List<CategoriesEntity> findByBlogAndNameContainingIgnoreCaseOrderBySequenceAsc(BlogsEntity blog, String name);

	// blog_id가 같고 category_id 가 같은 카테고리의 개수를 조회
	@Query("""
			    SELECT c.category.id, COUNT(c)
			    FROM ContentsEntity c
			    WHERE c.blog.id = :blogId AND c.category IS NOT NULL
			    GROUP BY c.category.id
			""")
	List<Object[]> countPostsByCategoryId(@Param("blogId") int blogId);
}

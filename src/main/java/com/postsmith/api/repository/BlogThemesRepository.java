package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.postsmith.api.domain.theme.dto.BlogThemesDto;
import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;



@Repository
public interface BlogThemesRepository extends JpaRepository<BlogThemesEntity, Integer> {
	List<BlogThemesEntity> findByBlogAndIsActiveTrue(BlogsEntity blog);

	@Modifying
	@Query("UPDATE BlogThemesEntity bt SET bt.isActive = false WHERE bt.blog = :blog AND bt.isActive = true")
	void deactivateAllThemesByBlog(@Param("blog") BlogsEntity blog);

}

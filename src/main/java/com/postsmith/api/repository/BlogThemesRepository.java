package com.postsmith.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.postsmith.api.domain.theme.dto.BlogThemesDto;
import com.postsmith.api.entity.BlogThemesEntity;
import com.postsmith.api.entity.BlogsEntity;


import com.postsmith.api.entity.BlogsEntity;

import java.util.Optional;

@Repository
public interface BlogThemesRepository extends JpaRepository<BlogThemesEntity, Integer> {

    // 블로그의 활성 테마 조회
    Optional<BlogThemesEntity> findByBlogAndIsActiveTrue(BlogsEntity blog);

    // 블로그 ID로 활성 테마 조회
    @Query("SELECT bt FROM BlogThemesEntity bt WHERE bt.blog.id = :blogId AND bt.isActive = true")
    Optional<BlogThemesEntity> findActiveByBlogId(@Param("blogId") Integer blogId);

    // 블로그의 모든 테마 비활성화
    @Modifying
    @Transactional
    @Query("UPDATE BlogThemesEntity bt SET bt.isActive = false WHERE bt.blog = :blog")
    void deactivateAllByBlog(@Param("blog") BlogsEntity blog);

    // 블로그의 특정 테마 활성화
    @Modifying
    @Transactional
    @Query("UPDATE BlogThemesEntity bt SET bt.isActive = true WHERE bt.id = :id")
    void activateById(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BlogThemesEntity bt SET bt.isActive = false WHERE bt.blog = :blog AND bt.isActive = true")
    void deactivateAllThemesByBlog(@Param("blog") BlogsEntity blog);
}

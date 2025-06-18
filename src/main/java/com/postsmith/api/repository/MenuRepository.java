package com.postsmith.api.repository;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	List<MenuEntity> findAllByBlog(BlogsEntity blog);

	void deleteAllByBlog(BlogsEntity blog);
}

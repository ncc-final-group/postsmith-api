package com.example.postsmith_api.repository.manage;

import com.example.postsmith_api.domain.blog.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    Optional<Categories> findByCategoryName(String categoryName);
}

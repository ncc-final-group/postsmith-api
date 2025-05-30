package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Blog;
import com.example.postsmith_api.domain.blog.BlogCategory;
import com.example.postsmith_api.domain.blog.Category;
import com.example.postsmith_api.dto.CategoryDto;
import com.example.postsmith_api.repository.manage.BlogCategoryRepository;
import com.example.postsmith_api.repository.manage.BlogRepository;
import com.example.postsmith_api.repository.manage.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BlogCategoryRepository blogCategoryRepository;
    private final BlogRepository blogRepository;
    public String createCategory(CategoryDto categoryDto, HttpRequest request) {
        Optional<Blog> opBlog = blogRepository.findById(categoryDto.getBlogId());
        if(opBlog.isEmpty()){
            return "Blog not found";
        }
        Blog blog = opBlog.get();
        Category category = null;
        category = opCategory.orElseGet(() -> categoryRepository.save(
                Category.builder()
                        .categoryName(categoryDto.getCategoryName())
                        .build()
        ));
        blogCategoryRepository.save(
                BlogCategory.builder()
                        .category(category)
                        .blog(blog).build()
        );
        return "Category created successfully";
    }
}

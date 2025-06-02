package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Blog;
import com.example.postsmith_api.domain.blog.Category;
import com.example.postsmith_api.dto.CategoryCreateDto;
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
    private final BlogRepository blogRepository;
    public String createCategory(CategoryCreateDto categoryDto) {
        Optional<Blog> opBlog = blogRepository.findById(categoryDto.getBlogId());
        if(opBlog.isEmpty()){
            return "Blog not found";
        }
        Blog blog = opBlog.get();
        Category category = Category.builder()
                .blogId(blog)
                .categoryName(categoryDto.getCategoryName())
                .parentCategoryId(categoryDto.getParentCategoryId()).build();
        categoryRepository.save(category);
        return "Category created successfully";
    }
}

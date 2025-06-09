package com.example.postsmith_api.service.manage;

import com.example.postsmith_api.domain.blog.Blogs;
import com.example.postsmith_api.domain.blog.Categories;
import com.example.postsmith_api.dto.CategoryCreateDto;
import com.example.postsmith_api.repository.manage.BlogRepository;
import com.example.postsmith_api.repository.manage.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BlogRepository blogRepository;
    public String createCategory(CategoryCreateDto categoryDto) {
        Optional<Blogs> opBlog = blogRepository.findById(categoryDto.getBlogId());
        if(opBlog.isEmpty()){
            return "Blog not found";
        }
        Blogs blog = opBlog.get();
        Categories category = Categories.builder()
                .blogId(blog)
                .categoryName(categoryDto.getCategoryName())
                .parentCategoryId(categoryDto.getParentCategoryId()).build();
        categoryRepository.save(category);
        return "Category created successfully";
    }
}

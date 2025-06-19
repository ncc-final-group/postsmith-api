package com.postsmith.api.domain.blog;

import com.postsmith.api.entity.BlogsEntity;
import com.postsmith.api.entity.UsersEntity;
import com.postsmith.api.repository.BlogsRepository;
import com.postsmith.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {
	private final BlogsRepository blogsRepository;
	private final UsersRepository usersRepository;

	@Transactional
	public String createBlog(BlogDto blogDto) {
		log.info("Creating blog: {}", blogDto);

		try {
			// 블로그 주소 중복 확인
			if (blogsRepository.existsByAddress(blogDto.getAddress())) {
				return "Blog URL already exists";
			}

			// 사용자 존재 확인
			UsersEntity user = usersRepository.findById(blogDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + blogDto.getUserId()));

			// 블로그 저장
			BlogsEntity blog = blogDto.toEntity(user);
			BlogsEntity savedBlog = blogsRepository.save(blog);

			log.info("Blog created successfully: id={}, address={}", savedBlog.getId(), savedBlog.getAddress());
			return "Blog created successfully";

		} catch (Exception e) {
			log.error("Error creating blog: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to create blog: " + e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	public BlogsEntity findBlogByAddress(String address) {
		return blogsRepository.findByAddress(address).orElseThrow(() -> new IllegalArgumentException("Blog not found with address: " + address));
	}

	@Transactional(readOnly = true)
	public BlogsEntity findBlogById(Integer id) {
		return blogsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + id));
	}

	@Transactional(readOnly = true)
	public List<BlogsEntity> findBlogsByUserId(Integer userId) {
		UsersEntity user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
		return blogsRepository.findByUserOrderByCreatedAtDesc(user);
	}

	@Transactional(readOnly = true)
	public boolean existsByAddress(String address) {
		return blogsRepository.existsByAddress(address);
	}

	@Transactional
	public String updateBlog(Integer id, BlogDto blogDto) {
		try {
			BlogsEntity blog = findBlogById(id);

			// 주소가 변경되었다면 중복 확인
			if (!blog.getAddress().equals(blogDto.getAddress()) && blogsRepository.existsByAddress(blogDto.getAddress())) {
				return "Blog URL already exists";
			}

			// 블로그 정보 업데이트
			blog.updateBlogInfo(blogDto.getName(), blogDto.getNickname(), blogDto.getAddress(), blogDto.getDescription(), blogDto.getLogoImage());

			BlogsEntity updatedBlog = blogsRepository.save(blog);
			log.info("Blog updated successfully: id={}, address={}", updatedBlog.getId(), updatedBlog.getAddress());

			return "Blog updated successfully";

		} catch (IllegalArgumentException e) {
			log.error("Blog not found for update: {}", id, e);
			throw e;
		} catch (Exception e) {
			log.error("Error updating blog: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to update blog: " + e.getMessage(), e);
		}
	}

	@Transactional
	public String deleteBlog(Integer id) {
		try {
			BlogsEntity blog = findBlogById(id);
			blogsRepository.delete(blog);

			log.info("Blog deleted successfully: id={}, address={}", blog.getId(), blog.getAddress());
			return "Blog deleted successfully";

		} catch (IllegalArgumentException e) {
			log.error("Blog not found for deletion: {}", id, e);
			throw e;
		} catch (Exception e) {
			log.error("Error deleting blog: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to delete blog: " + e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	public Long countBlogsByUserId(Integer userId) {
		UsersEntity user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
		return blogsRepository.countByUser(user);
	}



}

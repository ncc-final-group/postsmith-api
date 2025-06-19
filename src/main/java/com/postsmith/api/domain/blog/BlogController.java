package com.postsmith.api.domain.blog;

import com.postsmith.api.entity.BlogsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
	private final BlogService blogService;

	@PostMapping("/create")
	public ResponseEntity<String> createBlog(@RequestBody BlogDto blogDto) {
		try {
			String result = blogService.createBlog(blogDto);
			if (result.equals("Blog created successfully")) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.badRequest().body(result);
			}
		} catch (Exception e) {
			log.error("Error creating blog: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error creating blog: " + e.getMessage());
		}
	}

	@GetMapping("/address/{address}")
	public ResponseEntity<BlogsEntity> getBlogByAddress(@PathVariable String address) {
		try {
			BlogsEntity blog = blogService.findBlogByAddress(address);
			return ResponseEntity.ok(blog);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found: {}", address, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting blog by address: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<BlogsEntity> getBlogById(@PathVariable Integer id) {
		try {
			BlogsEntity blog = blogService.findBlogById(id);
			return ResponseEntity.ok(blog);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found with id: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error getting blog by id: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/address/{address}/exists")
	public ResponseEntity<Boolean> checkAddressExists(@PathVariable String address) {
		try {
			blogService.findBlogByAddress(address);
			return ResponseEntity.ok(true);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.ok(false);
		} catch (Exception e) {
			log.error("Error checking address existence: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<BlogsEntity>> getUserBlogs(@PathVariable Integer userId) {
		try {
			List<BlogsEntity> blogs = blogService.findBlogsByUserId(userId);
			return ResponseEntity.ok(blogs);
		} catch (Exception e) {
			log.error("Error getting user blogs: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateBlog(@PathVariable Integer id, @RequestBody BlogDto blogDto) {
		try {
			String result = blogService.updateBlog(id, blogDto);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found for update: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error updating blog: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error updating blog: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBlog(@PathVariable Integer id) {
		try {
			String result = blogService.deleteBlog(id);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			log.error("Blog not found for deletion: {}", id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Error deleting blog: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error deleting blog: " + e.getMessage());
		}
	}
}
package com.postsmith.api.domain.feedContent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postsmith.api.entity.*;
import com.postsmith.api.domain.feedContent.dto.SubscriptionDto;
import com.postsmith.api.repository.*;
import com.postsmith.api.domain.theme.dto.BlogsDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private final SubscriptionRepository subscriptionRepository;
	private final UsersRepository usersRepository;
	private final BlogsRepository blogsRepository;

	// 구독
	@Transactional
	public void subscribe(SubscriptionDto request) {
		UsersEntity subscriber = usersRepository.findById(request.getSubscriberId()).orElseThrow(() -> new IllegalArgumentException("Subscriber not found"));

		BlogsEntity blog = blogsRepository.findById(request.getBlogId()).orElseThrow(() -> new IllegalArgumentException("Blogs not found"));

		SubscriptionId id = new SubscriptionId(subscriber.getId(), blog.getId());

		if (subscriptionRepository.existsById(id)) {
			throw new IllegalStateException("Already subscribed.");
		}

		SubscriptionEntity subscription = SubscriptionEntity.builder().subscriber(subscriber).blog(blog).build();

		subscriptionRepository.save(subscription);
	}

	// 추천 구독 블로그
	public List<BlogsDto> findrecommendedBlogs(Integer userId) {
		List<BlogsEntity> blogs = blogsRepository.findrecommendedBlogs(userId);
		System.out.print(blogs);
		return blogs.stream().map(BlogsDto::fromEntity).collect(Collectors.toList());
	}

	// 구독
	public void subscribeBlog(Integer subscriberId, Integer blogId) {
		int inserted = blogsRepository.insertSubscription(subscriberId, blogId);

		if (inserted == 0) {
			throw new RuntimeException("해당 블로그가 존재하지 않습니다.");
		}
	}

}

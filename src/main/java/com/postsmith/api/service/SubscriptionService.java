package com.postsmith.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postsmith.api.dto.SubscriptionRequestDto;
import com.postsmith.api.entity.*;
import com.postsmith.api.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UsersRepository usersRepository;
    private final BlogsRepository blogsRepository;

    // 구독
    @Transactional
    public void subscribe(SubscriptionRequestDto request) {
        UsersEntity subscriber = usersRepository.findById(request.getSubscriberId())
            .orElseThrow(() -> new IllegalArgumentException("Subscriber not found"));

        BlogsEntity blog = blogsRepository.findById(request.getBlogId())
            .orElseThrow(() -> new IllegalArgumentException("Blogger not found"));

        SubscriptionId id = new SubscriptionId(subscriber.getId(), blog.getId());

        if (subscriptionRepository.existsById(id)) {
            throw new IllegalStateException("Already subscribed.");
        }

        SubscriptionEntity subscription = SubscriptionEntity.builder()
            .subscriber(subscriber)
            .blog(blog)
            .build();

        subscriptionRepository.save(subscription);
    }

}

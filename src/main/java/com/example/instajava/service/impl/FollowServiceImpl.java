package com.example.instajava.service.impl;

import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Follow;
import com.example.instajava.models.User;
import com.example.instajava.repository.FollowRepository;
import com.example.instajava.service.FollowService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    @Transactional
    public boolean followUser(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null || followerId.equals(followeeId)) {
            return false;
        }

        User follower = userService.findById(followerId);
        User followee = userService.findById(followeeId);

        if (followRepository.existsByFollowerIdAndFolloweeId(follower.getId(), followee.getId())) {
            return false;
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        followRepository.save(follow);
        return true;
    }

    @Override
    public long getUserFollowerCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return followRepository.countByFolloweeId(userId);
    }

    @Override
    public long getUserFollowingCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return followRepository.countByFollowerId(userId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null) {
            return false;
        }
        return followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    @Transactional
    public boolean unfollowUser(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null) {
            return false;
        }

        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return false;
        }

        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
        return true;
    }
}
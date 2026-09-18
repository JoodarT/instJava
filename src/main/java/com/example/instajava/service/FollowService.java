package com.example.instajava.service;

import com.example.instajava.dto.response.FollowResponseDto;

public interface FollowService {

    FollowResponseDto followUser(Long followeeId);

    FollowResponseDto unfollowUser(Long followeeId);

    long getUserFollowerCount(Long userId);

    long getUserFollowingCount(Long userId);

    boolean followUser(Long followerId, Long followeeId);

    boolean unfollowUser(Long followerId, Long followeeId);

    boolean isFollowing(Long followerId, Long followeeId);
}
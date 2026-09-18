package com.example.instajava.service;

import com.example.instajava.dto.response.FollowResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;

import java.util.List;

public interface FollowService {

    FollowResponseDto followUser(Long followeeId);

    FollowResponseDto unfollowUser(Long followeeId);

    long getUserFollowerCount(Long userId);

    long getUserFollowingCount(Long userId);

    boolean followUser(Long followerId, Long followeeId);

    boolean unfollowUser(Long followerId, Long followeeId);

    boolean isFollowing(Long followerId, Long followeeId);

    List<UserSummaryResponseDto> getFollowers(Long userId);

    List<UserSummaryResponseDto> getFollowing(Long userId);
}
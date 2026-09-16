package com.example.instajava.service.impl;

import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Follow;
import com.example.instajava.models.User;
import com.example.instajava.repository.FollowRepository;
import com.example.instajava.repository.UserRepository;
import com.example.instajava.service.FollowService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public boolean followUser(Long followerId, Long followeeId) {

        User follower = userService.findById(followerId).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с id " + followerId + " не найден")
        );

        User followee = userService.findById(followeeId).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с id " + followeeId + " не найден")
        );

        if(isFollowing(follower.getId(), followee.getId())) {
            return false;
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        follow.setCreatedAt(LocalDateTime.now());

        followRepository.save(follow);
        return true;
    }

    @Override
    public long getUserFollowerCount(Long userId) {

        if(!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }

        return followRepository.countByFolloweeId(userId);
    }

    @Override
    public long getUserFollowingCount(Long userId) {

        if(!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }

        return followRepository.countByFollowerId(userId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {

        if(!userService.existsUserById(followerId)) {
            throw new ResourceNotFoundException("Пользователь с id " + followerId + " не найден");
        }

        if(!userService.existsUserById(followeeId)) {
            throw new ResourceNotFoundException("Пользователь с id " + followeeId + " не найден");
        }

        return followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public boolean unfollowUser(Long followerId, Long followeeId) {

        if(!isFollowing(followerId, followeeId)) {
            return false;
        }

        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
        return true;
    }


}

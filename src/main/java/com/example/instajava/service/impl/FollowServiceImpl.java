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
            log.warn("Некорректная попытка подписки: followerId={}, followeeId={}", followerId, followeeId);
            return false;
        }

        User follower = userService.findById(followerId);
        User followee = userService.findById(followeeId);

        if (followRepository.existsByFollowerIdAndFolloweeId(follower.getId(), followee.getId())) {
            log.debug("Пользователь '{}' (id={}) уже подписан на '{}' (id={})",
                    follower.getUsername(), followerId, followee.getUsername(), followeeId);
            return false;
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        followRepository.save(follow);
        log.info("Пользователь '{}' (id={}) успешно подписался на '{}' (id={})",
                follower.getUsername(), followerId, followee.getUsername(), followeeId);
        return true;
    }

    @Override
    public long getUserFollowerCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            log.warn("Запрос количества подписчиков: пользователь с id={} не найден", userId);
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        long count = followRepository.countByFolloweeId(userId);
        log.debug("Количество подписчиков пользователя id={}: {}", userId, count);
        return count;
    }

    @Override
    public long getUserFollowingCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            log.warn("Запрос количества подписок: пользователь с id={} не найден", userId);
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        long count = followRepository.countByFollowerId(userId);
        log.debug("Количество подписок пользователя id={}: {}", userId, count);
        return count;
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null) {
            return false;
        }
        boolean following = followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
        log.debug("Проверка подписки followerId={} -> followeeId={}: {}", followerId, followeeId, following);
        return following;
    }

    @Override
    @Transactional
    public boolean unfollowUser(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null) {
            log.warn("Некорректная попытка отписки: followerId={}, followeeId={}", followerId, followeeId);
            return false;
        }

        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            log.debug("Подписка не найдена для удаления: followerId={}, followeeId={}", followerId, followeeId);
            return false;
        }

        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
        log.info("Пользователь id={} успешно отписался от пользователя id={}", followerId, followeeId);
        return true;
    }
}
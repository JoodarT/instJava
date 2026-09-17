package com.example.instajava.service.impl;

import com.example.instajava.models.Like;
import com.example.instajava.models.Post;
import com.example.instajava.models.User;
import com.example.instajava.repository.LikeRepository;
import com.example.instajava.service.LikeService;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Override
    @Transactional
    public boolean likePost(Long postId, Long userId) {
        if (postId == null || userId == null) {
            log.warn("Некорректная попытка поставить лайк: postId={}, userId={}", postId, userId);
            return false;
        }

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            log.debug("Пользователь id={} уже поставил лайк посту id={}", userId, postId);
            return false;
        }

        User user = userService.findById(userId);
        Post post = postService.getPostEntityById(postId);

        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();

        likeRepository.save(like);
        log.info("Пользователь '{}' (id={}) поставил лайк посту id={}", user.getUsername(), userId, postId);
        return true;
    }

    @Override
    @Transactional
    public boolean unlikePost(Long postId, Long userId) {
        if (postId == null || userId == null) {
            log.warn("Некорректная попытка убрать лайк: postId={}, userId={}", postId, userId);
            return false;
        }

        if (!likeRepository.existsByUserIdAndPostId(userId, postId)) {
            log.debug("Лайк от пользователя id={} к посту id={} не найден для удаления", userId, postId);
            return false;
        }

        likeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Пользователь id={} убрал лайк с поста id={}", userId, postId);
        return true;
    }

    @Override
    public long getPostLikesCount(Long postId) {
        long count = likeRepository.countByPostId(postId);
        log.debug("Количество лайков у поста id={}: {}", postId, count);
        return count;
    }

    @Override
    public boolean isPostLikedByUser(Long postId, Long userId) {
        if (postId == null || userId == null) {
            return false;
        }
        boolean liked = likeRepository.existsByUserIdAndPostId(userId, postId);
        log.debug("Проверка лайка: postId={}, userId={}, isLiked={}", postId, userId, liked);
        return liked;
    }
}
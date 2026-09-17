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
        log.info("Пользователь '{}' поставил лайк посту id={}", user.getUsername(), postId);
        return true;
    }

    @Override
    @Transactional
    public boolean unlikePost(Long postId, Long userId) {
        if (postId == null || userId == null) {
            return false;
        }

        if (!likeRepository.existsByUserIdAndPostId(userId, postId)) {
            return false;
        }

        likeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Пользователь id={} убрал лайк с поста id={}", userId, postId);
        return true;
    }

    @Override
    public long getPostLikesCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public boolean isPostLikedByUser(Long postId, Long userId) {
        if (postId == null || userId == null) {
            return false;
        }
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
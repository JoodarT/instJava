package com.example.instajava.service.impl;

import com.example.instajava.dto.response.LikeResponseDto;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Like;
import com.example.instajava.models.Post;
import com.example.instajava.models.User;
import com.example.instajava.repository.LikeRepository;
import com.example.instajava.repository.UserRepository;
import com.example.instajava.service.LikeService;
import com.example.instajava.service.PostService;
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
    private final UserRepository userRepository;
    private final PostService postService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public LikeResponseDto toggleLike(Long postId) {
        User currentUser = currentUserProvider.getRequiredCurrentUser();
        Long userId = currentUser.getId();

        boolean alreadyLiked = isPostLikedByUser(postId, userId);

        if (alreadyLiked) {
            unlikePost(postId, userId);
            log.info("Пользователь '{}' убрал лайк с поста id={}", currentUser.getUsername(), postId);
        } else {
            likePost(postId, userId);
            log.info("Пользователь '{}' поставил лайк посту id={}", currentUser.getUsername(), postId);
        }

        long updatedLikesCount = getPostLikesCount(postId);

        return LikeResponseDto.builder()
                .postId(postId)
                .likesCount(updatedLikesCount)
                .liked(!alreadyLiked)
                .build();
    }

    @Override
    @Transactional
    public boolean likePost(Long postId, Long userId) {
        if (postId == null || userId == null) {
            return false;
        }

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            return false;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        Post post = postService.getPostEntityById(postId);

        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();

        likeRepository.save(like);
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

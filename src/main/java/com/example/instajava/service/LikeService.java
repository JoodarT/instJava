package com.example.instajava.service;

import com.example.instajava.dto.response.LikeResponseDto;

public interface LikeService {

    /**
     * Переключить лайк текущего пользователя (поставить / снять)
     */
    LikeResponseDto toggleLike(Long postId);

    boolean likePost(Long postId, Long userId);

    boolean unlikePost(Long postId, Long userId);

    long getPostLikesCount(Long postId);

    boolean isPostLikedByUser(Long postId, Long userId);
}
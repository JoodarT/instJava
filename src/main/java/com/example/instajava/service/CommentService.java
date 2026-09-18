package com.example.instajava.service;

import com.example.instajava.dto.request.CommentCreateRequestDto;
import com.example.instajava.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(Long postId, CommentCreateRequestDto request);

    CommentResponseDto addComment(Long postId, Long userId, String text);

    List<CommentResponseDto> getCommentsByPostId(Long postId);

    void deleteComment(Long commentId);

    void deleteComment(Long commentId, Long currentUserId);

    long getPostCommentsCount(Long postId);
}
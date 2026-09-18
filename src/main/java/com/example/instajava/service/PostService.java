package com.example.instajava.service;

import com.example.instajava.dto.request.PostCreateRequestDto;
import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostCreateRequestDto request);

    PostResponseDto createPost(MultipartFile image, String caption, Long authorId);

    PostResponseDto getPostById(Long postId);

    List<PostResponseDto> getUserPosts(Long userId);

    List<PostResponseDto> getUserPosts(String username);

    List<PostResponseDto> getFeed();

    List<PostResponseDto> getFeed(Long currentUserId);

    void deletePost(Long postId);

    void deletePost(Long postId, Long currentUserId);

    long getUserPostCount(Long userId);

    Post getPostEntityById(Long postId);
}
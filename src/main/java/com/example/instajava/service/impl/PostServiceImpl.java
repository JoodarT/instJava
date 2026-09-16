package com.example.instajava.service.impl;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Post;
import com.example.instajava.repository.PostRepository;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public long getUserPostCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return postRepository.countByAuthorId(userId);
    }

    @Override
    public PostResponseDto createPost(MultipartFile image, String caption, Long authorId) {
        return null;
    }

    @Override
    public void deletePost(Long postId, Long currentUserId) {

    }

    @Override
    public List<PostResponseDto> getFeed(Long currentUserId) {
        return List.of();
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        return null;
    }

    @Override
    public Post getPostEntityById(Long postId) {
        return null;
    }

    @Override
    public List<PostResponseDto> getUserPosts(Long userId) {
        return List.of();
    }
}
package com.example.instajava.service.impl;

import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.repository.PostRepository;
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
}
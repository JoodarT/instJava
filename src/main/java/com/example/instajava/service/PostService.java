package com.example.instajava.service;

public interface PostService {

    /**
     * Получение количества публикаций пользователя.
     */
    long getUserPostCount(Long userId);
}
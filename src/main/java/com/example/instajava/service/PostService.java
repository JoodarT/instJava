package com.example.instajava.service;

public interface PostService {

    /**
     * Получение количества публикаций пользователя.
     * @param userId ID пользователя.
     * @return Количество публикаций.
     */
    long getUserPostCount(Long userId);
}

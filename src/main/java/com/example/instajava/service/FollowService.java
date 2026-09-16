package com.example.instajava.service;

public interface FollowService {

    /**
     * Получение количества подписчиков пользователя.
     */
    long getUserFollowerCount(Long userId);

    /**
     * Получение количества подписок пользователя.
     */
    long getUserFollowingCount(Long userId);

    /**
     * Оформление подписки на другого пользователя.
     * @return true, если подписка создана; false, если уже был подписан или попытка подписки на себя.
     */
    boolean followUser(Long followerId, Long followeeId);

    /**
     * Отмена подписки.
     * @return true, если подписка удалена; false, если подписки не существовало.
     */
    boolean unfollowUser(Long followerId, Long followeeId);

    /**
     * Проверка существования подписки.
     */
    boolean isFollowing(Long followerId, Long followeeId);
}
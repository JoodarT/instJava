package com.example.instajava.service;

public interface FollowService {

    /**
     * Получение количества подписчиков пользователя.
     * @param userId ID пользователя.
     * @return Количество подписчиков.
     */
    long getUserFollowerCount(Long userId);

    /**
     * Получение количества подписок пользователя.
     * @param userId ID пользователя.
     * @return Количество подписок.
     */
    long getUserFollowingCount(Long userId);

    /**
     * Оформление подписки на другого пользователя.
     * @param followerId ID пользователя, который подписывается.
     * @param followeeId ID пользователя, на которого подписываются.
     * @return True, если подписка успешно оформлена, false если уже был подписан.
     */
    boolean followUser(Long followerId, Long followeeId);

    /**
     * Отмена подписки на другого пользователя.
     * @param followerId ID пользователя, который отписывается.
     * @param followeeId ID пользователя, от которого отписываются.
     * @return True, если подписка успешно отменена, false если не был подписан.
     */
    boolean unfollowUser(Long followerId, Long followeeId);

    /**
     * Проверка, подписан ли пользователь follower на пользователя followee.
     * @param followerId ID пользователя-подписчика.
     * @param followeeId ID пользователя-цели подписки.
     * @return True, если подписан, иначе false.
     */
    boolean isFollowing(Long followerId, Long followeeId);
}

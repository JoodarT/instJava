package com.example.instajava.service;

public interface LikeService {

    /**
     * Поставить отметку «Мне нравится» публикации.
     * Бизнес-правило: один и тот же пользователь может лайкнуть публикацию только один раз.
     *
     * @param postId ID публикации.
     * @param userId ID пользователя, ставящего лайк.
     * @return true, если лайк успешно поставлен; false, если лайк уже стоял ранее.
     */
    boolean likePost(Long postId, Long userId);

    /**
     * Убрать отметку «Мне нравится» с публикации (дизлайк/анлайк).
     *
     * @param postId ID публикации.
     * @param userId ID пользователя.
     * @return true, если лайк успешно удален; false, если лайка не было.
     */
    boolean unlikePost(Long postId, Long userId);

    /**
     * Получение общего количества лайков у публикации (для счетчика).
     *
     * @param postId ID публикации.
     * @return количество лайков.
     */
    long getPostLikesCount(Long postId);

    /**
     * Проверка, поставил ли конкретный пользователь лайк на эту публикацию.
     * Используется на фронтенде для закрашивания иконки сердечка.
     *
     * @param postId ID публикации.
     * @param userId ID пользователя.
     * @return true, если лайк поставлен, иначе false.
     */
    boolean isPostLikedByUser(Long postId, Long userId);
}
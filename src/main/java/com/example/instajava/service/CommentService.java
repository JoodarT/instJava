package com.example.instajava.service;

import com.example.instajava.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    /**
     * Добавление нового комментария к публикации.
     *
     * @param postId ID публикации.
     * @param userId ID автора комментария.
     * @param text текст комментария.
     * @return DTO созданного комментария.
     */
    CommentResponseDto addComment(Long postId, Long userId, String text);

    /**
     * Получение всех комментариев публикации.
     * По ТЗ: комментарии должны быть отсортированы хронологически (от самого раннего к новым).
     *
     * @param postId ID публикации.
     * @return список DTO комментариев.
     */
    List<CommentResponseDto> getCommentsByPostId(Long postId);

    /**
     * Удаление комментария.
     * Бизнес-правило по ТЗ: "Можно удалить любые комментарии, но ТОЛЬКО под своей публикацией"
     * (т.е. автор поста модерирует любые комментарии под своими постами).
     *
     * @param commentId ID удаляемого комментария.
     * @param currentUserId ID текущего авторизованного пользователя.
     * @throws AccessDeniedException если текущий пользователь не является автором поста.
     */
    void deleteComment(Long commentId, Long currentUserId);

    /**
     * Получение количества комментариев у публикации (для счетчика).
     *
     * @param postId ID публикации.
     * @return число комментариев.
     */
    long getPostCommentsCount(Long postId);
}
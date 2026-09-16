package com.example.instajava.service;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    /**
     * Создание новой публикации.
     * Сохраняет переданное изображение на диск, связывает с автором и текстом описания.
     *
     * @param image файл изображения публикации.
     * @param caption текст описания (подпись к фото).
     * @param authorId ID автора публикации.
     * @return DTO созданной публикации.
     */
    PostResponseDto createPost(MultipartFile image, String caption, Long authorId);

    /**
     * Получение детальной информации о публикации по её ID.
     *
     * @param postId ID публикации.
     * @return DTO публикации (автор, картинка, описание, дата, счетчики лайков и комментов).
     * @throws ResourceNotFoundException если публикация не найдена.
     */
    PostResponseDto getPostById(Long postId);

    /**
     * Получение всех публикаций конкретного пользователя для сетки в его профиле.
     * Сортировка: от самых новых к старым.
     *
     * @param userId ID пользователя.
     * @return список DTO публикаций пользователя.
     */
    List<PostResponseDto> getUserPosts(Long userId);

    /**
     * Получение новостной ленты для текущего пользователя.
     * Включает публикации пользователей, на которых он подписан, отсортированные по убыванию даты создания.
     *
     * @param currentUserId ID текущего авторизованного пользователя.
     * @return список публикаций для ленты.
     */
    List<PostResponseDto> getFeed(Long currentUserId);

    /**
     * Удаление публикации.
     * Бизнес-правило: удалить публикацию может ТОЛЬКО её автор.
     * При удалении каскадно удаляются все связанные лайки, комментарии и файл картинки с диска.
     *
     * @param postId ID публикации.
     * @param currentUserId ID пользователя, инициировавшего удаление (для проверки прав).
     * @throws AccessDeniedException если пользователь пытается удалить чужой пост.
     */
    void deletePost(Long postId, Long currentUserId);

    /**
     * Получение количества публикаций пользователя (для счетчика в профиле).
     *
     * @param userId ID пользователя.
     * @return число публикаций.
     */
    long getUserPostCount(Long userId);

    /**
     * Внутренний метод: получение JPA-сущности Post по ID.
     * Используется внутри LikeService и CommentService для связи @ManyToOne.
     *
     * @param postId ID публикации.
     * @return JPA-сущность Post.
     * @throws ResourceNotFoundException если публикация не найдена.
     */
    Post getPostEntityById(Long postId);
}
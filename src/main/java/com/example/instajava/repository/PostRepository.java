package com.example.instajava.repository;

import com.example.instajava.models.Post;
import com.example.instajava.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Все публикации пользователя для страницы профиля (от новых к старым)
     */
    List<Post> findAllByAuthorOrderByCreatedAtDesc(User author);

    List<Post> findAllByAuthorIdOrderByCreatedAtDesc(Long authorId);

    /**
     * Количество публикаций пользователя (для счетчика в профиле)
     */
    long countByAuthorId(Long authorId);

    /**
     * Лента новостей: посты авторов, на которых подписан пользователь,
     * отсортированные от самых новых к старым.
     */
    @Query("""
            SELECT p FROM Post p
            JOIN FETCH p.author
            WHERE p.author IN (
                SELECT f.followee FROM Follow f WHERE f.follower.id = :userId
            )
            ORDER BY p.createdAt DESC
            """)
    List<Post> findFeedByUserId(@Param("userId") Long userId);

    /**
     * Получение поста вместе с автором одним запросом (для страницы поста)
     */
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);
}
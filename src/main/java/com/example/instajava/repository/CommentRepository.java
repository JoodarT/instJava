package com.example.instajava.repository;

import com.example.instajava.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Комментарии к посту в хронологическом порядке (от самого первого)
     */
    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            WHERE c.post.id = :postId
            ORDER BY c.createdAt ASC
            """)
    List<Comment> findAllByPostIdOrderByCreatedAtAsc(@Param("postId") Long postId);

    /**
     * Количество комментариев под постом (для счетчика)
     */
    long countByPostId(Long postId);

    /**
     * Поиск комментария с подгрузкой поста и автора (для проверки прав на удаление)
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.post p JOIN FETCH p.author WHERE c.id = :id")
    Optional<Comment> findByIdWithPostAndAuthor(@Param("id") Long id);
}
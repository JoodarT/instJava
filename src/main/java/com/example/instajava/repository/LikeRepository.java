package com.example.instajava.repository;

import com.example.instajava.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Проверка, лайкнул ли пользователь публикацию (для блокировки повторного лайка)
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * Количество лайков на публикации
     */
    long countByPostId(Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
package com.example.instajava.repository;

import com.example.instajava.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Проверка, подписан ли пользователь follower на followee
     */
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    /**
     * Количество подписчиков пользователя (followers)
     */
    long countByFolloweeId(Long followeeId);

    /**
     * Количество подписок пользователя (following)
     */
    long countByFollowerId(Long followerId);

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
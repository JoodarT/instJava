package com.example.instajava.controller.api;

import com.example.instajava.dto.response.LikeResponseDto;
import com.example.instajava.models.User;
import com.example.instajava.service.LikeService;
import com.example.instajava.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Likes API", description = "Управление лайками публикаций")
public class LikeRestController {

    private final LikeService likeService;
    private final UserService userService;

    @Operation(summary = "Переключить отметку 'Мне нравится' (поставить / убрать)")
    @PostMapping("/like")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId) {
        User currentUser = userService.getCurrentUser()
                .orElseThrow(() -> new AccessDeniedException("Для установки лайка требуется авторизация"));

        Long userId = currentUser.getId();
        boolean alreadyLiked = likeService.isPostLikedByUser(postId, userId);

        if (alreadyLiked) {
            likeService.unlikePost(postId, userId);
            log.info("Пользователь '{}' убрал лайк с поста id={}", currentUser.getUsername(), postId);
        } else {
            likeService.likePost(postId, userId);
            log.info("Пользователь '{}' поставил лайк посту id={}", currentUser.getUsername(), postId);
        }

        long updatedLikesCount = likeService.getPostLikesCount(postId);

        LikeResponseDto response = LikeResponseDto.builder()
                .postId(postId)
                .likesCount(updatedLikesCount)
                .liked(!alreadyLiked)
                .build();

        return ResponseEntity.ok(response);
    }
}
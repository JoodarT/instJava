package com.example.instajava.controller.api;

import com.example.instajava.dto.response.LikeResponseDto;
import com.example.instajava.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
@Tag(name = "Likes API", description = "Управление лайками публикаций")
public class LikeRestController {

    private final LikeService likeService;

    @Operation(summary = "Переключить отметку 'Мне нравится' (поставить / убрать)")
    @PostMapping("/like")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.toggleLike(postId));
    }
}
package com.example.instajava.controller.api;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Feed API", description = "Персональная лента новостей")
public class FeedRestController {

    private final PostService postService;

    @Operation(summary = "Лента текущего пользователя: посты авторов, на которых он подписан")
    @GetMapping("/api/feed")
    public ResponseEntity<List<PostResponseDto>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }
}

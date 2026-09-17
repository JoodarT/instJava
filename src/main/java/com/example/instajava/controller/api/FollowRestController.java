package com.example.instajava.controller.api;

import com.example.instajava.dto.response.FollowResponseDto;
import com.example.instajava.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
@Tag(name = "Follows API", description = "Управление подписками на пользователей")
public class FollowRestController {

    private final FollowService followService;

    @Operation(summary = "Подписаться на пользователя")
    @PostMapping("/follow")
    public ResponseEntity<FollowResponseDto> followUser(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.followUser(userId));
    }

    @Operation(summary = "Отписаться от пользователя")
    @PostMapping("/unfollow")
    public ResponseEntity<FollowResponseDto> unfollowUser(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.unfollowUser(userId));
    }
}
package com.example.instajava.controller.api;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import com.example.instajava.service.impl.UserProfileFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users API", description = "Поиск, публичные профили и публикации пользователей")
public class UserRestController {

    private final UserService userService;
    private final PostService postService;
    private final UserProfileFacade userProfileFacade;

    @Operation(summary = "Поиск пользователей по логину, email или имени")
    @GetMapping("/search")
    public ResponseEntity<List<UserSummaryResponseDto>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(userService.search(query));
    }

    @Operation(summary = "Публичный профиль пользователя со счетчиками")
    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(userProfileFacade.getUserProfile(username));
    }

    @Operation(summary = "Публикации пользователя (для сетки в профиле)")
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@PathVariable String username) {
        return ResponseEntity.ok(postService.getUserPosts(username));
    }
}

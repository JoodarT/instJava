package com.example.instajava.controller.api;

import com.example.instajava.dto.request.CommentCreateRequestDto;
import com.example.instajava.dto.response.CommentResponseDto;
import com.example.instajava.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comments API", description = "Управление комментариями")
public class CommentRestController {

    private final CommentService commentService;

    @Operation(summary = "Получить все комментарии к публикации")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @Operation(summary = "Добавить комментарий к публикации")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long postId,
                                                         @Valid @RequestBody CommentCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(postId, request));
    }

    @Operation(summary = "Удалить комментарий (только автор публикации)")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
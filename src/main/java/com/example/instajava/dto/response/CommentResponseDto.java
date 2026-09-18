package com.example.instajava.dto.response;

import com.example.instajava.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long postId;
    private String text;
    private LocalDateTime createdAt;

    private UserSummaryResponseDto author;

    private boolean canDelete;

    public static CommentResponseDto from(Comment comment, boolean canDelete) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .author(UserSummaryResponseDto.fromEntity(comment.getAuthor()))
                .canDelete(canDelete)
                .build();
    }
}
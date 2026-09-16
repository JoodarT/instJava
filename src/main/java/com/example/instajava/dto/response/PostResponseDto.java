package com.example.instajava.dto.response;

import com.example.instajava.models.Post;
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
public class PostResponseDto {

    private Long id;
    private String imagePath;
    private String caption;
    private LocalDateTime createdAt;

    // Информация об авторе публикации (аватар, логин, имя)
    private UserSummaryResponseDto author;

    // Счетчики
    private long likesCount;
    private long commentsCount;

    // Флаги контекста (для UI кнопок)
    private boolean isLikedByCurrentUser; // закрашивать ли сердечко
    private boolean isOwner;               // показывать ли кнопку "Удалить публикацию"

    public static PostResponseDto from(Post post,
                                       long likesCount,
                                       long commentsCount,
                                       boolean isLikedByCurrentUser,
                                       boolean isOwner) {
        return PostResponseDto.builder()
                .id(post.getId())
                .imagePath(post.getImagePath())
                .caption(post.getCaption())
                .createdAt(post.getCreatedAt())
                .author(UserSummaryResponseDto.fromEntity(post.getAuthor()))
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .isLikedByCurrentUser(isLikedByCurrentUser)
                .isOwner(isOwner)
                .build();
    }
}
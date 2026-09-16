package com.example.instajava.dto.response;

import com.example.instajava.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private Long id;
    private String username;
    private String fullName;
    private String bio;
    private String avatarPath;

    // Счетчики для профиля
    private long postsCount;
    private long followersCount;
    private long followingCount;

    // Флаги для отображения кнопок (Подписаться / Отписаться / Редактировать)
    private boolean isFollowing;
    private boolean isCurrentUser;

    public static UserProfileResponseDto from(User user,
                                           long postsCount,
                                           long followersCount,
                                           long followingCount,
                                           boolean isFollowing,
                                           boolean isCurrentUser) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .avatarPath(user.getAvatarPath())
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .isFollowing(isFollowing)
                .isCurrentUser(isCurrentUser)
                .build();
    }
}
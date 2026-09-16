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
public class UserSummaryResponseDto {

    private Long id;
    private String username;
    private String fullName;
    private String avatarPath;

    public static UserSummaryResponseDto fromEntity(User user) {
        return UserSummaryResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatarPath(user.getAvatarPath())
                .build();
    }
}
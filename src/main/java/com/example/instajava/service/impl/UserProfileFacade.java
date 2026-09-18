package com.example.instajava.service.impl;

import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.models.User;
import com.example.instajava.service.FollowService;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileFacade {

    private final UserService userService;
    private final PostService postService;
    private final FollowService followService;

    public UserProfileResponseDto getUserProfile(String username) {
        User targetUser = userService.findByUsername(username);

        long postsCount = postService.getUserPostCount(targetUser.getId());
        long followersCount = followService.getUserFollowerCount(targetUser.getId());
        long followingCount = followService.getUserFollowingCount(targetUser.getId());

        Optional<User> currentUserOpt = userService.getCurrentUser();

        boolean isCurrentUser = currentUserOpt
                .map(cur -> cur.getId().equals(targetUser.getId()))
                .orElse(false);

        boolean isFollowing = currentUserOpt
                .map(cur -> followService.isFollowing(cur.getId(), targetUser.getId()))
                .orElse(false);

        return UserProfileResponseDto.from(
                targetUser,
                postsCount,
                followersCount,
                followingCount,
                isFollowing,
                isCurrentUser
        );
    }
}

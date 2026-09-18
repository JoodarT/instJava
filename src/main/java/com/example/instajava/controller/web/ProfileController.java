package com.example.instajava.controller.web;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.service.PostService;
import com.example.instajava.service.impl.UserProfileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileFacade userProfileFacade;
    private final PostService postService;

    @GetMapping("/users/{username}")
    public String profile(@PathVariable String username, Model model) {
        UserProfileResponseDto profile = userProfileFacade.getUserProfile(username);
        List<PostResponseDto> posts = postService.getUserPosts(username);

        model.addAttribute("profile", profile);
        model.addAttribute("posts", posts);
        return "profile";
    }
}

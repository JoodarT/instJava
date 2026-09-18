package com.example.instajava.controller.web;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final PostService postService;

    @GetMapping("/")
    public String feed(Model model) {
        List<PostResponseDto> posts = postService.getFeed();
        model.addAttribute("posts", posts);
        return "feed";
    }
}

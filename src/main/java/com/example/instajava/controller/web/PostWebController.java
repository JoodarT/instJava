package com.example.instajava.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostWebController {

    @GetMapping("/posts/new")
    public String newPostForm() {
        return "posts/new";
    }
}

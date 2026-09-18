package com.example.instajava.controller.web;

import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final UserService userService;

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String q, Model model) {
        List<UserSummaryResponseDto> results = userService.search(q);
        model.addAttribute("query", q);
        model.addAttribute("results", results);
        return "search";
    }
}

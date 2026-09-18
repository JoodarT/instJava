package com.example.instajava.controller.web;

import com.example.instajava.models.User;
import com.example.instajava.service.impl.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Кладёт текущего пользователя в модель каждой веб-страницы (для навигации в layout.ftlh),
 * чтобы не дублировать этот вызов в каждом контроллере.
 */
@ControllerAdvice(basePackages = "com.example.instajava.controller.web")
@RequiredArgsConstructor
public class GlobalWebModelAttributes {

    private final CurrentUserProvider currentUserProvider;

    @ModelAttribute("currentUser")
    public User currentUser() {
        return currentUserProvider.getCurrentUser().orElse(null);
    }
}

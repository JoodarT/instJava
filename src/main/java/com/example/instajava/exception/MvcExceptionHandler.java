package com.example.instajava.exception;

import com.example.instajava.service.impl.CurrentUserProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "com.example.instajava.controller.web")
public class MvcExceptionHandler {

    private final CurrentUserProvider currentUserProvider;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException e, HttpServletRequest request, Model model) {
        log.warn("404 на {}: {}", request.getRequestURI(), e.getMessage());
        return renderError(model, 404, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException e, HttpServletRequest request, Model model) {
        log.warn("403 на {}: {}", request.getRequestURI(), e.getMessage());
        return renderError(model, 403, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException e, HttpServletRequest request, Model model) {
        log.warn("400 на {}: {}", request.getRequestURI(), e.getMessage());
        return renderError(model, 400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception e, HttpServletRequest request, Model model) {
        log.error("Необработанная ошибка на {}", request.getRequestURI(), e);
        return renderError(model, 500, "Внутренняя ошибка сервера");
    }

    private String renderError(Model model, int status, String message) {
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        model.addAttribute("currentUser", currentUserProvider.getCurrentUser().orElse(null));
        return "error";
    }
}

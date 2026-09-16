package com.example.instajava.service;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.dto.response.UserResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Регистрация нового пользователя.
     */
    UserResponseDto register(RegistrationRequest request);

    /**
     * Поиск пользователей по имени, логину или email.
     */
    List<UserSummaryResponseDto> search(String query);

    /**
     * Получение DTO профиля пользователя со всеми счетчиками (для страницы /users/{username}).
     */
    UserProfileResponseDto getUserProfile(String username);

    /**
     * Получение сущности пользователя по ID (для взаимодействия между сервисами).
     */
    User findById(Long id);

    /**
     * Получение сущности пользователя по логину.
     */
    User findByUsername(String username);

    /**
     * Получение сущности пользователя по email.
     */
    User findByEmail(String email);

    /**
     * Получение текущего авторизованного пользователя из контекста Spring Security.
     * Возвращает Optional.empty(), если запрос делает неавторизованный гость.
     */
    Optional<User> getCurrentUser();

    /**
     * Проверка существования пользователя по ID.
     */
    boolean existsUserById(Long userId);
}
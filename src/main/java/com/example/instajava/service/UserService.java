package com.example.instajava.service;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Регистрация нового пользователя.
     * @param request Данные для регистрации.
     * @return Зарегистрированный пользователь.
     * @throws DuplicateUserExceptio Если имя пользователя или email уже заняты.
     */
    User register(RegistrationRequest request);

    /**
     * Поиск пользователей по имени, логину или email.
     * @param query Строка поиска.
     * @return Список найденных пользователей.
     */
    List<User> search(String query);

    /**
     * Получение пользователя по его ID.
     * @param id ID пользователя.
     * @return Optional пользователя.
     */
    Optional<User> findById(Long id);

    /**
     * Получение пользователя по его логину (username).
     * @param username Логин пользователя.
     * @return Optional пользователя.
     */
    Optional<User> findByUsername(String username);

    /**
     * Получение пользователя по его email.
     * @param email Email пользователя.
     * @return Optional пользователя.
     */
    Optional<User> findByEmail(String email);

    /**
     * Получение текущего авторизованного пользователя.
     * (Реализация будет зависеть от Spring Security Context).
     * @return Optional текущего пользователя.
     */
    Optional<User> getCurrentUser();



    boolean existsUserById(Long userId);
}
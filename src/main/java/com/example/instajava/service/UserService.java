package com.example.instajava.service;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.dto.response.UserResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto register(RegistrationRequest request);

    List<UserSummaryResponseDto> search(String query);

    UserProfileResponseDto getUserProfile(String username);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> getCurrentUser();

    /**
     * Получить текущего авторизованного пользователя или выбросить AccessDeniedException
     */
    User getRequiredCurrentUser();

    boolean existsUserById(Long userId);
}
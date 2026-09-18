package com.example.instajava.service;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.dto.response.UserResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto register(RegistrationRequest request);

    List<UserSummaryResponseDto> search(String query);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> getCurrentUser();

    User getRequiredCurrentUser();

    boolean existsUserById(Long userId);
}
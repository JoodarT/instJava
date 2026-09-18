package com.example.instajava.service.impl;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.dto.response.UserProfileResponseDto;
import com.example.instajava.dto.response.UserResponseDto;
import com.example.instajava.dto.response.UserSummaryResponseDto;
import com.example.instajava.exception.DuplicateUserException;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.User;
import com.example.instajava.repository.UserRepository;
import com.example.instajava.service.FollowService;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowService followService;
    private final PostService postService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public UserResponseDto register(RegistrationRequest request) {
        log.debug("Попытка регистрации пользователя: username='{}', email='{}'", request.username(), request.email());

        if (userRepository.existsByUsername(request.username())) {
            log.warn("Регистрация отклонена: логин '{}' занят", request.username());
            throw new DuplicateUserException("Username '" + request.username() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Регистрация отклонена: email '{}' уже занят", request.email());
            throw new DuplicateUserException("Email '" + request.email() + "' is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        User saved = userRepository.save(user);
        log.info("Пользователь зарегистрирован: id={}, username='{}'", saved.getId(), saved.getUsername());
        return UserResponseDto.fromEntity(saved);
    }

    @Override
    public List<UserSummaryResponseDto> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        log.debug("Поиск пользователей по запросу: '{}'", query);
        List<UserSummaryResponseDto> results = userRepository.search(query.trim()).stream()
                .map(UserSummaryResponseDto::fromEntity)
                .toList();
        log.debug("Найдено {} пользователей по запросу '{}'", results.size(), query);
        return results;
    }

    @Override
    public UserProfileResponseDto getUserProfile(String username) {
        log.debug("Запрос профиля пользователя: '{}'", username);
        User targetUser = findByUsername(username);

        long postsCount = postService.getUserPostCount(targetUser.getId());
        long followersCount = followService.getUserFollowerCount(targetUser.getId());
        long followingCount = followService.getUserFollowingCount(targetUser.getId());

        Optional<User> currentUserOpt = getCurrentUser();

        boolean isCurrentUser = currentUserOpt
                .map(cur -> cur.getId().equals(targetUser.getId()))
                .orElse(false);

        boolean isFollowing = currentUserOpt
                .map(cur -> followService.isFollowing(cur.getId(), targetUser.getId()))
                .orElse(false);

        return UserProfileResponseDto.from(
                targetUser,
                postsCount,
                followersCount,
                followingCount,
                isFollowing,
                isCurrentUser
        );
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResourceNotFoundException("Email не может быть пустым");
        }
        return userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email '" + email + "' не найден"));
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("ID пользователя не может быть null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Override
    public User findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResourceNotFoundException("Username не может быть пустым");
        }
        return userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь '" + username + "' не найден"));
    }

    @Override
    public Optional<User> getCurrentUser() {
        return currentUserProvider.getCurrentUser();
    }

    @Override
    public User getRequiredCurrentUser() {
        return currentUserProvider.getRequiredCurrentUser();
    }

    @Override
    public boolean existsUserById(Long userId) {
        if (userId == null) {
            return false;
        }
        return userRepository.existsById(userId);
    }
}

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowService followService;
    private final PostService postService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Lazy FollowService followService,
                           @Lazy PostService postService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followService = followService;
        this.postService = postService;
    }

    @Override
    @Transactional
    public UserResponseDto register(RegistrationRequest request) {
        log.debug("Попытка регистрации нового пользователя: username='{}', email='{}'", request.username(), request.email());

        if (userRepository.existsByUsername(request.username())) {
            log.warn("Регистрация отклонена: имя пользователя '{}' уже занято", request.username());
            throw new DuplicateUserException("Username '" + request.username() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Регистрация отклонена: email '{}' уже зарегистрирован", request.email());
            throw new DuplicateUserException("Email '" + request.email() + "' is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        User saved = userRepository.save(user);
        log.info("Новый пользователь успешно зарегистрирован: id={}, username='{}'", saved.getId(), saved.getUsername());
        return UserResponseDto.fromEntity(saved);
    }

    @Override
    public List<UserSummaryResponseDto> search(String query) {
        if (query == null || query.isBlank()) {
            log.debug("Поисковый запрос пользователей пуст");
            return List.of();
        }
        log.debug("Выполняется поиск пользователей по запросу: '{}'", query);
        List<UserSummaryResponseDto> results = userRepository.search(query.trim()).stream()
                .map(UserSummaryResponseDto::fromEntity)
                .toList();
        log.debug("По запросу '{}' найдено пользователей: {}", query, results.size());
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

        log.debug("Профиль пользователя '{}' сформирован: posts={}, followers={}, following={}, isFollowing={}, isCurrent={}",
                username, postsCount, followersCount, followingCount, isFollowing, isCurrentUser);

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
            log.warn("Попытка поиска пользователя с пустым email");
            throw new ResourceNotFoundException("Email не может быть пустым");
        }
        log.debug("Поиск пользователя по email: '{}'", email);
        return userRepository.findByEmail(email.trim())
                .orElseThrow(() -> {
                    log.warn("Пользователь с email '{}' не найден", email);
                    return new ResourceNotFoundException("Пользователь с email '" + email + "' не найден");
                });
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            log.warn("Попытка поиска пользователя с id=null");
            throw new ResourceNotFoundException("ID пользователя не может быть null");
        }
        log.debug("Поиск пользователя по id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id={} не найден", id);
                    return new ResourceNotFoundException("Пользователь с id " + id + " не найден");
                });
    }

    @Override
    public User findByUsername(String username) {
        if (username == null || username.isBlank()) {
            log.warn("Попытка поиска пользователя с пустым username");
            throw new ResourceNotFoundException("Username не может быть пустым");
        }
        log.debug("Поиск пользователя по username: '{}'", username);
        return userRepository.findByUsername(username.trim())
                .orElseThrow(() -> {
                    log.warn("Пользователь с username '{}' не найден", username);
                    return new ResourceNotFoundException("Пользователь '" + username + "' не найден");
                });
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return userRepository.findByUsername(auth.getName());
    }

    @Override
    public boolean existsUserById(Long userId) {
        if (userId == null) {
            return false;
        }
        return userRepository.existsById(userId);
    }
}
package com.example.instajava.service.impl;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.exception.DuplicateUserException;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.exception.UserAlreadyExistException;
import com.example.instajava.models.User;
import com.example.instajava.repository.UserRepository;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserException("Username '" + request.username() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException("Email '" + request.email() + "' is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        User saved = userRepository.save(user);
        log.info("User registered: id={}, username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public List<User> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return userRepository.search(query.trim());
    }

    @Override
    public Optional<User> findByEmail(String email) {

        if(userRepository.existsByEmail(email)) {
            return userRepository.findByEmail(email);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<User> findById(Long id) {

        if(existsUserById(id)) {
            return userRepository.findById(id);
        } else {
            throw new ResourceNotFoundException("No user with that ID was found");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {

        if(userRepository.existsByUsername(username)) {
            return userRepository.findByUsername(username);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }

    @Override
    public boolean existsUserById(Long userId) {
        return userRepository.existsById(userId);
    }
}

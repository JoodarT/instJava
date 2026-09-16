package com.example.instajava.service;

import com.example.instajava.dto.request.RegistrationRequest;
import com.example.instajava.models.User;

import java.util.List;

public interface UserService {

    User register(RegistrationRequest request);

    List<User> search(String query);
}

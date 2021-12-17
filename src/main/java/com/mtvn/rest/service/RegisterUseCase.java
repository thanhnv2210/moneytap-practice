package com.mtvn.rest.service;

import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.persistence.entities.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {
    private final UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }
}

package com.mtvn.rest.service.impl;

import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.persistence.entities.auth.User;
import com.mtvn.rest.service.RegisterUseCaseV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterUseCaseV3Impl implements RegisterUseCaseV3 {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
}

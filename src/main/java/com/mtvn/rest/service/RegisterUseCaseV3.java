package com.mtvn.rest.service;

import com.mtvn.persistence.entities.auth.User;

import java.util.Optional;

public interface RegisterUseCaseV3 {
    User registerUser(User user);
    Optional<User> findByPhone(String phone) ;
}

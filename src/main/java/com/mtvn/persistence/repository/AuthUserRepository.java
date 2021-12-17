package com.mtvn.persistence.repository;

import com.mtvn.persistence.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByPhone(String phone);
    Optional<AuthUser> findByEmail(String email);
}

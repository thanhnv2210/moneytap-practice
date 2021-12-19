package com.mtvn.rest.service;

import com.mtvn.persistence.entities.auth.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RegisterUseCaseV2Test {
    @Autowired
    private RegisterUseCase registerUseCase;

    @Test
    void savedUserHasRegistrationDate() {
        User user = new User();
        user.setUsername("david");
        user.setPassword("MyPassword");
        user.setName("Thanh Nguyen");
        user.setEmail("thanh@moneytap.vn");
        user.setPhone("389963096");
        System.out.println(user);
        User savedUser = registerUseCase.registerUser(user);
        System.out.println(savedUser);
        assertThat(savedUser).isNotNull();
        assertNotNull(user.getId());
        System.out.println("userId: " + user.getId());
    }
}

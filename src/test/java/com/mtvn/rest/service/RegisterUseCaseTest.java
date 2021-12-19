package com.mtvn.rest.service;

import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.persistence.entities.auth.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RegisterUseCaseTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUseCase registerUseCase;

    @Test
    void savedUserHasRegistrationDate() {
        User user = new User();
        user.setUsername("Thanhnv123");
        System.out.println(user);
        User savedUser = registerUseCase.registerUser(user);
        System.out.println(savedUser);
        assertThat(savedUser).isNotNull();
        assertNotNull(user.getId());
    }
}

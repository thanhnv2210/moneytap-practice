package com.mtvn.rest.service;

import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.persistence.entities.auth.User;
import com.mtvn.rest.service.impl.RegisterUseCaseV3Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RegisterUseCaseMockitoTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUseCaseV3Impl registerUseCaseV3;

    @BeforeEach
    public void setUp() {
        User thanh = new User();
        thanh.setUsername("david");
        thanh.setPassword("MyPassword");
        thanh.setName("Thanh Nguyen");
        thanh.setEmail("thanh@moneytap.vn");
        thanh.setPhone("389963096");

        Mockito.when(userRepository.findByPhone(thanh.getPhone()))
                .thenReturn(Optional.of(thanh));
    }

    @Test
    void findByPhone_ReturnExistUser() {
        Optional<User> findUser = registerUseCaseV3.findByPhone("389963096");
        System.out.println(findUser);
        assertThat(findUser).isPresent();
        assertEquals("david", findUser.get().getUsername());
    }
}

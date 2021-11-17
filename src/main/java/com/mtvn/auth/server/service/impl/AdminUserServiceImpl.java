package com.mtvn.auth.server.service.impl;

import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.auth.server.service.AdminUserService;
import com.mtvn.common.string.StringUtils;
import com.mtvn.persistence.entities.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserByPhoneOrEmail(String phone, String email) {
        if(StringUtils.hasText(phone))
            return userRepository.findByPhone(phone);
        if(StringUtils.hasText(email))
            return userRepository.findByEmail(email);
        return Optional.empty();
    }

    @Override
    public List<UserDetailModel> getAllUsers() {
        return userRepository.findAll().stream().map(UserDetailModel::fromEntity).collect(Collectors.toList());
    }

    @Override
    public User createNewUser(UserDetailModel userDetailModel) {
        if(getUserByPhoneOrEmail(userDetailModel.getPhone(),userDetailModel.getEmail()).isPresent())
            throw new RuntimeException("User already exist");
        User user = userDetailModel.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        Optional<User> targetUser = userRepository.findById(id);
        targetUser.ifPresent(user -> userRepository.delete(user));
    }
}

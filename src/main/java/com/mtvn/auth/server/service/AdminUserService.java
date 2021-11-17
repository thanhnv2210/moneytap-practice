package com.mtvn.auth.server.service;

import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.persistence.entities.auth.User;

import java.util.List;
import java.util.Optional;

public interface AdminUserService {
    Optional<User> getUserByPhoneOrEmail(String phone, String email);
    List<UserDetailModel> getAllUsers();
    User createNewUser(UserDetailModel userDetailModel);
    void delete(Integer id);
}
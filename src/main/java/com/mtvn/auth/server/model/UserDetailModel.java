package com.mtvn.auth.server.model;

import com.mtvn.persistence.entities.auth.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailModel {
    String username;
    String password;
    String name;
    String email;
    String phone;
    String orgCode;
    Boolean accountEnabled;
    Boolean resetPassword;
    List<String> roles;
    List<String> groups;

    public User toEntity() {
        val newUser = new User();
        BeanUtils.copyProperties(this, newUser);
        return newUser;
    }

    public static UserDetailModel fromEntity(User user) {
        val result = new UserDetailModel();
        BeanUtils.copyProperties(user, result);
        return result;
    }

    public static UserDetailModel fromRoleAndGroup(User user, List<UserRole> roles, List<UserGroup> groups){
        val result = new UserDetailModel();
        BeanUtils.copyProperties(user, result);
        result.roles = roles.stream().map(UserRole::getRoleName).collect(Collectors.toList());
        result.groups = groups.stream().map(UserGroup::getGroupName).collect(Collectors.toList());
        return result;
    }
}

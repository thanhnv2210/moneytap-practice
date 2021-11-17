package com.mtvn.persistence.entities.auth;

import com.mtvn.persistence.entities.template.BaseEntityIntID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mt_idm_user")
@Data
public class User extends BaseEntityIntID {
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "org_code")
    private String orgCode;
    @Column(name = "manager_id")
    private String managerId;
    @Column(name = "account_enabled")
    private boolean accountEnabled;
    @Column(name = "reset_password")
    private boolean resetPassword;
}

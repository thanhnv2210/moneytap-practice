package com.mtvn.auth.server.model;

import lombok.Data;

@Data
public class AuthenticationRequestModel {
    String username;
    String password;
}

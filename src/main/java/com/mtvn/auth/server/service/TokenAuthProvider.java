package com.mtvn.auth.server.service;

import com.mtvn.auth.server.model.AuthenticationResponseModel;
import com.mtvn.auth.server.model.UserDetailModel;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface TokenAuthProvider {
    AuthenticationResponseModel generateToken(UserDetailModel user) throws NoSuchAlgorithmException, InvalidKeySpecException;
}

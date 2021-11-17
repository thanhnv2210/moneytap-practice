package com.mtvn.auth.server.service;

import com.mtvn.auth.server.model.MoneytapUserAuthenticationToken;
import com.mtvn.auth.server.model.UserDetailModel;

import java.util.Optional;

public interface TokenAuthVerifier {
    Optional<UserDetailModel> verifyToken(String token);
    Optional<MoneytapUserAuthenticationToken> authenticateToken(String token);
}

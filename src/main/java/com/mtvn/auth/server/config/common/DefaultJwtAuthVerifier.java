package com.mtvn.auth.server.config.common;

import com.mtvn.auth.server.config.AuthClientConfig;
import com.mtvn.auth.server.model.MoneytapUserAuthenticationToken;
import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.service.TokenAuthVerifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class DefaultJwtAuthVerifier implements TokenAuthVerifier {
    @Autowired
    AuthClientConfig authClientConfig;

    public DefaultJwtAuthVerifier(AuthClientConfig authClientConfig) {
        this.authClientConfig = authClientConfig;
    }

    @Override
    public Optional<UserDetailModel> verifyToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<MoneytapUserAuthenticationToken> authenticateToken(String token) {
        return Optional.empty();
    }
}

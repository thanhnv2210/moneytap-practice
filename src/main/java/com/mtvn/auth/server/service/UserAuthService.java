package com.mtvn.auth.server.service;

import com.mtvn.auth.server.model.AuthenticationRequestModel;
import com.mtvn.auth.server.model.AuthenticationResponseModel;
import com.mtvn.auth.server.model.JwkCertificateModel;
import com.mtvn.auth.server.model.UserDetailModel;

import java.util.List;
import java.util.Optional;

public interface UserAuthService {
    Optional<AuthenticationResponseModel> getToken(AuthenticationRequestModel model);
    Optional<UserDetailModel> verifyToken(String token);
    List<JwkCertificateModel> getCertificates();
}

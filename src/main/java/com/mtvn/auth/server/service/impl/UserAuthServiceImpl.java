package com.mtvn.auth.server.service.impl;

import com.mtvn.auth.server.model.AuthenticationRequestModel;
import com.mtvn.auth.server.model.AuthenticationResponseModel;
import com.mtvn.auth.server.model.JwkCertificateModel;
import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.repository.*;
import com.mtvn.auth.server.service.JwtAuthVerifier;
import com.mtvn.auth.server.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private UserRoleRepository roleRepository;
    @Autowired private UserGroupRepository groupRepository;
    @Autowired private JwtAuthProvider authProvider;
    @Autowired private JwtAuthVerifier authVerifier;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Optional<AuthenticationResponseModel> getToken(AuthenticationRequestModel model) {
        Optional<UserDetailModel> userDetailModel = loadUser(model.getUsername());
        if(userDetailModel.isPresent()){
            log.info("input:{} db:{}", model.getPassword(), userDetailModel.get().getPassword());
            if(passwordEncoder.matches(model.getPassword(), userDetailModel.get().getPassword())){
                return Optional.of(authProvider.generateToken(userDetailModel.get()));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDetailModel> verifyToken(String token) {
        return authVerifier.verifyToken(token);
    }

    @Override
    public List<JwkCertificateModel> getCertificates() {
        return authProvider.getJwkCertificates();
    }

    private Optional<UserDetailModel> loadUser(String username) {
        val user = userRepository.findByUsername(username);
        if(user.isPresent()){
            val roles = roleRepository.findByUsername(username);
            val groups = groupRepository.findByUsername(username);
            return Optional.of(UserDetailModel.fromRoleAndGroup(user.get(), roles, groups));
        }
        return Optional.empty();
    }
}

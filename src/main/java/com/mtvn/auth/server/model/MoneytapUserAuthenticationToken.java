package com.mtvn.auth.server.model;

import com.mtvn.auth.server.enums.InternalAuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MoneytapUserAuthenticationToken implements Authentication {
    // TODO will update this class later
    private Optional<UserDetailModel> userDetailModel;
    private InternalAuthenticationType authType = InternalAuthenticationType.UNKNOWN;
    private String token;

    public MoneytapUserAuthenticationToken(
            Optional<UserDetailModel> userDetailModel, String token){
        this.userDetailModel = userDetailModel;
        this.token = token;
    }

    private boolean isAuthenticated = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userDetailModel.isPresent()){
            final List<String> roles = userDetailModel.get().getRoles();
            if (roles != null && roles.size() > 0){
                return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated && userDetailModel.isPresent() && userDetailModel.get().getAccountEnabled();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.userDetailModel.map(UserDetailModel::getUsername).orElse(null);
    }
}

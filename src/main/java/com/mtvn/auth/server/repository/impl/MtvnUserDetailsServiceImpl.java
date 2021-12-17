package com.mtvn.auth.server.repository.impl;

import com.mtvn.auth.server.repository.MtvnUserDetailsService;
import com.mtvn.persistence.dao.MtvnUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MtvnUserDetailsServiceImpl implements MtvnUserDetailsService {
    @Override
    public MtvnUserDetail findByPhone(String phone) {
        return null;
    }

    @Override
    public MtvnUserDetail findByUserName(String userName) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

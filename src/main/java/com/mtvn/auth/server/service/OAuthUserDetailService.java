package com.mtvn.auth.server.service;

import com.mtvn.auth.server.repository.MtvnUserDetailsService;
import com.mtvn.persistence.dao.MtvnUserDetail;
import com.mtvn.persistence.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OAuthUserDetailService implements MtvnUserDetailsService {

    @Autowired
    private AuthUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found"));
    }

    @Override
    public MtvnUserDetail findByUserName(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found"));
    }

    @Override
    public MtvnUserDetail findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
}

package com.mtvn.auth.server.repository;

import com.mtvn.persistence.dao.MtvnUserDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MtvnUserDetailsService extends UserDetailsService {
    MtvnUserDetail findByPhone(String phone);
    MtvnUserDetail findByUserName(String userName) throws UsernameNotFoundException;
}

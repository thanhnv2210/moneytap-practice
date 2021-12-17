package com.mtvn.persistence.dao;

import com.mtvn.enums.AuthenticationType;
import org.springframework.security.core.userdetails.UserDetails;

public interface MtvnUserDetail extends UserDetails {
    AuthenticationType getAuthType();
}

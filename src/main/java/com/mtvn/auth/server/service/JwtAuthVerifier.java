package com.mtvn.auth.server.service;

import com.mtvn.auth.server.config.common.ApplicationAuthClientConfig;
import com.mtvn.auth.server.config.common.DefaultJwtAuthVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthVerifier extends DefaultJwtAuthVerifier {
    public JwtAuthVerifier(@Autowired ApplicationAuthClientConfig applicationAuthClientConfig){
        super(applicationAuthClientConfig);
    }
}

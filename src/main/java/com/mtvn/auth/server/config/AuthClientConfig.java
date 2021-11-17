package com.mtvn.auth.server.config;

import lombok.Data;

@Data
public class AuthClientConfig {
    String issuer;
    String jwkUrl;
    Long jwkCacheTimeInMinutes;
}

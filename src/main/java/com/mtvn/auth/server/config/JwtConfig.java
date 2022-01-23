package com.mtvn.auth.server.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@Slf4j
public class JwtConfig {
    String issuer = "moneytap";
    List<JwkCertificate> certificates;

    @PostConstruct
    private void init(){
        log.info("issuer:{}",issuer);
    }


    @Data
    public static class JwkCertificate {
        String keyId;
        String keyType;
        String algorithm;
        String publicKey;
        String privateKey;
    }
}

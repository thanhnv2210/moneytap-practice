package com.mtvn.auth.server.config.common;

import com.mtvn.auth.server.config.AuthClientConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "app.auth.client.jwt")
@Data
@Slf4j
public class ApplicationAuthClientConfig extends AuthClientConfig {
    String issuer = "moneytap";
    String jwkUrl = null;
    Long jwkCacheTimeInMinutes  = 30L;
    @PostConstruct
    private void init(){
        log.info("ApplicationAuthClientConfig issuer:{} jwkUrl:{} jwkCacheTimeInMinutes:{}", issuer,jwkUrl, jwkCacheTimeInMinutes);
    }
}

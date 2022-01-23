package com.mtvn.auth.server.config.common;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.mtvn.auth.server.config.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service("RSAPublicKeyProviderImpl")
public class RSAPublicKeyProviderImpl implements RSAKeyProvider {
    @Autowired private JwtConfig JwtConfig;

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        Optional<com.mtvn.auth.server.config.JwtConfig.JwkCertificate> first = JwtConfig.getCertificates().stream().filter(o -> o.getKeyId().equals(kid)).findFirst();
        return first.map(c -> {
            try {
                val kf = KeyFactory.getInstance("RSA");
                val keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(c.getPublicKey()));
                return (RSAPublicKey) kf.generatePublic(keySpecX509);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return null;
            }
        }).orElseGet(null);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}

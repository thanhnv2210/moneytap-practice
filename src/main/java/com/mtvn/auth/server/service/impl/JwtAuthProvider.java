package com.mtvn.auth.server.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mtvn.auth.server.config.AuthClientConfig;
import com.mtvn.auth.server.config.JwtConfig;
import com.mtvn.auth.server.config.JwtConstant;
import com.mtvn.auth.server.model.AuthenticationResponseModel;
import com.mtvn.auth.server.model.JwkCertificateModel;
import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.service.TokenAuthProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class JwtAuthProvider implements TokenAuthProvider {
    @Autowired private JwtConfig JwtConfig;
    @Autowired private AuthClientConfig authClientConfig;

    @Override
    public AuthenticationResponseModel generateToken(UserDetailModel user) {
        val certificate = JwtConfig.getCertificates().stream().findFirst().orElse(null);
        val algorithm = getAlgorithm(certificate);

        val now = ZonedDateTime.now();
        val expiry = now.plusSeconds(60 * 60 * 3);
        val issuedAt = Date.from(now.toInstant());
        val expiresAt = Date.from(expiry.toInstant());
        val token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(JwtConstant.EMAIL, user.getEmail())
                .withClaim(JwtConstant.PHONE, user.getPhone())
                .withClaim(JwtConstant.ACCOUNT_ENABLED, user.getAccountEnabled())
                .withClaim(JwtConstant.RESET_PASSWORD, user.getResetPassword())
                .withArrayClaim(JwtConstant.ROLES, user.getRoles().toArray(new String[0]))
                .withArrayClaim(JwtConstant.GROUPS, user.getGroups().toArray(new String[0]))
                .withClaim(JwtConstant.ORG, user.getOrgCode())
                .withIssuer(JwtConfig.getIssuer())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withKeyId(certificate.getKeyId())
                .sign(algorithm);
        return AuthenticationResponseModel.builder()
                .token(token)
                .expiresAt(expiresAt)
                .build();
    }

    private Algorithm getAlgorithm(com.mtvn.auth.server.config.JwtConfig.JwkCertificate certificate) {
        try {
            val kf = KeyFactory.getInstance("RSA");
            val keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(certificate.getPrivateKey()));
            val privateKey = (RSAPrivateKey)kf.generatePrivate(keySpecPKCS8);

            val keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(certificate.getPublicKey()));
            val publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

            return Algorithm.RSA256(publicKey, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex){
            throw new RuntimeException(ex);
        }
    }

    public List<JwkCertificateModel> getJwkCertificates(){
        return JwtConfig.getCertificates().stream()
                .map(c -> {
                    try {
                        val kf = KeyFactory.getInstance("RSA");
                        val keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(c.getPublicKey()));
                        val publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
                        val modulus = Base64.getEncoder().encodeToString(publicKey.getModulus().toByteArray());
                        val exponent = Base64.getEncoder().encodeToString(publicKey.getPublicExponent().toByteArray());
                        return JwkCertificateModel.builder()
                                .keyId(c.getKeyId())
                                .keyType(c.getKeyType())
                                .algorithm(c.getAlgorithm())
                                .modulus(modulus)
                                .exponent(exponent)
                                .build();
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        //e.printStackTrace();
                        return null;
                    }
                    })
                .collect(Collectors.toList());
    }
}

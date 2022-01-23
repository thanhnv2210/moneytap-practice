package com.mtvn.auth.server.config.common;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.mtvn.auth.server.config.AuthClientConfig;
import com.mtvn.auth.server.config.JwtConstant;
import com.mtvn.auth.server.enums.InternalAuthenticationType;
import com.mtvn.auth.server.model.MoneytapUserAuthenticationToken;
import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.service.TokenAuthVerifier;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class DefaultJwtAuthVerifier implements TokenAuthVerifier {
    @Autowired
    AuthClientConfig authClientConfig;
    @Autowired
    @Qualifier("RSAPublicKeyProviderImpl")
    RSAKeyProvider rsaKeyProvider;

    private JwkProvider jwkProvider ;

    public DefaultJwtAuthVerifier(AuthClientConfig authClientConfig) {
        this.authClientConfig = authClientConfig;
    }

    @Override
    public Optional<UserDetailModel> verifyToken(String token) {
        Optional<DecodedJWT> decodedJWT = verifyTokenAndGetJwt(token);
        if(decodedJWT.isPresent()){
            DecodedJWT o = decodedJWT.get();
            Map<String, Claim> claims = decodedJWT.get().getClaims();
            UserDetailModel result = new UserDetailModel();
            result.setName(o.getSubject());
            Optional.ofNullable(claims).ifPresent(c -> {
                Optional.ofNullable(c.get(JwtConstant.ROLES)).ifPresent(d -> result.setRoles(d.asList(String.class)));
                Optional.ofNullable(c.get(JwtConstant.GROUPS)).ifPresent(d -> result.setGroups(d.asList(String.class)));
                Optional.ofNullable(c.get(JwtConstant.ORG)).ifPresent(d -> result.setOrgCode(d.asString()));
                Optional.ofNullable(c.get(JwtConstant.EMAIL)).ifPresent(d -> result.setEmail(d.asString()));
                Optional.ofNullable(c.get(JwtConstant.PHONE)).ifPresent(d -> result.setPhone(d.asString()));
                Optional.ofNullable(c.get(JwtConstant.ACCOUNT_ENABLED)).ifPresent(d -> result.setAccountEnabled(d.asBoolean()));
                Optional.ofNullable(c.get(JwtConstant.RESET_PASSWORD)).ifPresent(d -> result.setResetPassword(d.asBoolean()));
            });
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<MoneytapUserAuthenticationToken> authenticateToken(String token) {
        return verifyToken(token).map(o -> new MoneytapUserAuthenticationToken(Optional.of(o), InternalAuthenticationType.JWT, token));
    }

    Optional<DecodedJWT> verifyTokenAndGetJwt(String token){
        try {
            val algorithm = Algorithm.RSA256(rsaKeyProvider); //getRSAPublicKeyProvider().map(Algorithm::RSA256).orElseGet(null);
            val jwtVerifier = JWT.require(algorithm).acceptLeeway(1)
                    .withIssuer(authClientConfig.getIssuer()).build();
            return Optional.ofNullable(jwtVerifier.verify(token));
        }
        catch (Exception e ) {
            throw new IllegalStateException("Something went wrong while decoding the JWT token: $token", e);
        }
    }

    Optional<RSAKeyProvider> getRSAPublicKeyProvider(){
        return Optional.of(new RSAPublicKeyProviderImpl());//jwkProvider.get()
    }
}



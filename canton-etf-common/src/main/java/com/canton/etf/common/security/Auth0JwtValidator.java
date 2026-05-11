package com.canton.etf.common.security;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Component
public class Auth0JwtValidator {

    private final String issuer;
    private final String audience;
    private final JwkProvider jwkProvider;

    public Auth0JwtValidator(
            @Value("${auth0.issuer}") String issuer,
            @Value("${auth0.audience}") String audience) throws Exception {
        this.issuer = issuer;
        this.audience = audience;
        this.jwkProvider = new JwkProviderBuilder(new URL(issuer + ".well-known/jwks.json"))
                .build();
    }

    public DecodedJWT validate(String token) {
        try {
            DecodedJWT decoded = JWT.decode(token);
            var jwk = jwkProvider.get(decoded.getKeyId());
            var algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new SecurityException("Invalid JWT: " + e.getMessage());
        }
    }

    public String extractPartyId(DecodedJWT jwt) {
        return jwt.getClaim("canton_party_id").asString();
    }
}
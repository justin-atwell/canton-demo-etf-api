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
    private final boolean devMode;

    public Auth0JwtValidator(
            @Value("${auth0.issuer}") String issuer,
            @Value("${auth0.audience}") String audience,
            @Value("${auth0.dev-mode:false}") boolean devMode) throws Exception {
        this.issuer = issuer;
        this.audience = audience;
        this.devMode = devMode;
        this.jwkProvider = new JwkProviderBuilder(new URL(issuer + ".well-known/jwks.json"))
                .build();
    }

    public DecodedJWT validate(String token) {
        if (devMode) {
            // In dev mode, skip signature validation and just decode
            return JWT.decode(token);
        }
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
        if (devMode) {
            // In dev mode, return the party from the Authorization header value directly
            // if no canton_party_id claim exists
            String partyId = jwt.getClaim("canton_party_id").asString();
            return partyId != null ? partyId : "FundManager::12204a9df91dd8b99f843f82b34a593f83afe82b6a6c66b078478b863c2cc341a0de";
        }
        return jwt.getClaim("canton_party_id").asString();
    }

    public boolean isDevMode() {
        return devMode;
    }

    public String getDevPartyId(String role) {
        return switch (role) {
            case "ComplianceOfficer" -> "ComplianceOfficer::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "Custodian"         -> "Custodian::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "Auditor"           -> "Auditor::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "MarketMaker"       -> "MarketMaker::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "operator"          -> "operator::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "HedgeFund"         -> "HedgeFund::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "PrimeBroker"       -> "PrimeBroker::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            case "RiskManager"       -> "RiskManager::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
            default                  -> "FundManager::122054c77909ff7f1e4182de6ff982ab3d8c0d93eb98e3bb1a456ff58304ce4df02a";
        };
    }
}
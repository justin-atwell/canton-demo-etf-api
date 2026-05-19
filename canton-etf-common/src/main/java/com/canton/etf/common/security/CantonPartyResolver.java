package com.canton.etf.common.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class CantonPartyResolver {

    private final Auth0JwtValidator jwtValidator;

    public CantonPartyResolver(Auth0JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    public String resolveParty(String bearerToken) {
        if (jwtValidator.isDevMode()) {
            String role = extractDevRole(bearerToken);
            return jwtValidator.getDevPartyId(role);
        }
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT jwt = jwtValidator.validate(token);
        return jwtValidator.extractPartyId(jwt);
    }

    private String extractDevRole(String bearerToken) {
        // Convention: "Bearer dev-ComplianceOfficer" → "ComplianceOfficer"
        if (bearerToken != null && bearerToken.startsWith("Bearer dev-")) {
            return bearerToken.replace("Bearer dev-", "");
        }
        return "FundManager";
    }
}
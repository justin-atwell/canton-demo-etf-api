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
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT jwt = jwtValidator.validate(token);
        String partyId = jwtValidator.extractPartyId(jwt);
        if (partyId == null || partyId.isBlank()) {
            throw new SecurityException("No canton_party_id claim in JWT");
        }
        return partyId;
    }
}
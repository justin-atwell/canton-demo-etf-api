package com.canton.etf.api.controller;

import com.canton.etf.api.service.AuditService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;
    private final CantonPartyResolver partyResolver;

    public AuditController(AuditService auditService, CantonPartyResolver partyResolver) {
        this.auditService = auditService;
        this.partyResolver = partyResolver;
    }

    @GetMapping
    public ResponseEntity<?> getAccessEvents(
            @RequestHeader("Authorization") String authHeader){
        String partyId = partyResolver.resolveParty(authHeader);

        return ResponseEntity.ok(auditService.getAccessEvents(partyId));
    }
}

package com.canton.etf.api.controller;

import com.canton.etf.api.dto.SubstitutionRequestDto;
import com.canton.etf.api.service.SubstitutionRequestService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prime/substitutions")
public class SubstitutionRequestController {

    private final SubstitutionRequestService substitutionRequestService;
    private final CantonPartyResolver partyResolver;

    public SubstitutionRequestController(SubstitutionRequestService substitutionRequestService,
                                         CantonPartyResolver partyResolver) {
        this.substitutionRequestService = substitutionRequestService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SubstitutionRequestDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(substitutionRequestService.createRequest(partyId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequest(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(substitutionRequestService.getRequest(partyId, id));
    }

    // PrimeBroker approves
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveByCustodian(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        String comment = body.getOrDefault("comment", "");
        return ResponseEntity.ok(substitutionRequestService.approveByCustodian(partyId, id, comment));
    }

    // PrimeBroker rejects
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectByBroker(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        String reason = body.getOrDefault("reason", "");
        return ResponseEntity.ok(substitutionRequestService.rejectByBroker(partyId, id, reason));
    }

    // Custodian confirms transfer — atomically updates CollateralPool
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmTransfer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(substitutionRequestService.confirmTransfer(partyId, id));
    }

    @GetMapping
    public ResponseEntity<?> getRequests(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(substitutionRequestService.getRequests(partyId));
    }

    // Custodian rejects
    @PostMapping("/{id}/custodian-reject")
    public ResponseEntity<?> rejectByCustodian(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        String reason = body.getOrDefault("reason", "");
        return ResponseEntity.ok(substitutionRequestService.rejectByCustodian(partyId, id, reason));
    }
}
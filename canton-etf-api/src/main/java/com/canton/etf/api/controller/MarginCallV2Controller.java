package com.canton.etf.api.controller;

import com.canton.etf.api.dto.MarginCallV2Dto;
import com.canton.etf.api.service.MarginCallV2Service;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.ResponseType;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prime/margin-calls")
public class MarginCallV2Controller {

    private final MarginCallV2Service marginCallV2Service;
    private final CantonPartyResolver partyResolver;

    public MarginCallV2Controller(MarginCallV2Service marginCallV2Service,
                                  CantonPartyResolver partyResolver) {
        this.marginCallV2Service = marginCallV2Service;
        this.partyResolver = partyResolver;
    }

    // PrimeBroker issues a margin call
    @PostMapping
    public ResponseEntity<?> issueMarginCall(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MarginCallV2Dto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(marginCallV2Service.issueMarginCall(partyId, request));
    }

    // Returns all ISSUED or RESPONSE_RECEIVED calls visible to the calling party
    @GetMapping("/active")
    public ResponseEntity<?> getActiveMarginCalls(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(marginCallV2Service.getActiveMarginCalls(partyId));
    }

    // HedgeFund responds within responseDeadline window
    @PostMapping("/{id}/respond")
    public ResponseEntity<?> respondToCall(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        ResponseType responseType = ResponseType.valueOf(body.getOrDefault("responseType", "POSTADDITIONALCOLLATERAL"));
        String comment = body.getOrDefault("comment", "");
        return ResponseEntity.ok(marginCallV2Service.respondToCall(partyId, id, responseType, comment));
    }

    // PrimeBroker manually marks satisfied
    @PostMapping("/{id}/satisfy")
    public ResponseEntity<?> satisfyCall(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, Double> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        double newCoverage = body.getOrDefault("newCoverage", 0.0);
        return ResponseEntity.ok(marginCallV2Service.satisfyCall(partyId, id, newCoverage));
    }

    // PrimeBroker declares default — requires deadline passed
    @PostMapping("/{id}/default")
    public ResponseEntity<?> declareDefault(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(marginCallV2Service.declareDefault(partyId, id));
    }

    // PrimeBroker updates coverage — auto-satisfies when threshold met
    @PostMapping("/{id}/coverage")
    public ResponseEntity<?> updateCoverage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Map<String, Double> body) {
        String partyId = partyResolver.resolveParty(authHeader);
        double newCoverage = body.getOrDefault("currentCoverage", 0.0);
        return ResponseEntity.ok(marginCallV2Service.updateCoverage(partyId, id, newCoverage));
    }
}
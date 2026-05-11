package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.api.service.EtfService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etf")
public class EtfController {

    private final EtfService etfService;
    private final CantonPartyResolver partyResolver;

    public EtfController(
            EtfService etfService,
            CantonPartyResolver partyResolver) {
        this.etfService = etfService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<String> createEtf(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateEtfRequest request) {
        String partyId = partyResolver.resolveParty(authHeader);
        String contractId = etfService.createEtf(partyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(contractId);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getEtf(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(etfService.getEtf(partyId, ticker));
    }

    @PutMapping("/{ticker}/suspend")
    public ResponseEntity<Void> suspendEtf(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker) {
        String partyId = partyResolver.resolveParty(authHeader);
        etfService.suspendEtf(partyId, ticker);
        return ResponseEntity.noContent().build();
    }
}
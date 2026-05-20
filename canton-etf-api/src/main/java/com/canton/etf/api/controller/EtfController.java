package com.canton.etf.api.controller;

import com.canton.etf.api.dto.AddConstituentRequest;
import com.canton.etf.api.dto.ConstituentResponse;
import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.api.service.EtfService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<String>> listEtfs(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(etfService.listEtfs(partyId));
    }

    @PutMapping("/{ticker}/suspend")
    public ResponseEntity<Void> suspendEtf(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker) {
        String partyId = partyResolver.resolveParty(authHeader);
        etfService.suspendEtf(partyId, ticker);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ticker}/constituent")
    public ResponseEntity<String> addConstituent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker,
            @RequestBody AddConstituentRequest request) {
        String partyId = partyResolver.resolveParty(authHeader);
        String symbol = etfService.addConstituent(partyId, ticker, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(symbol);
    }

    @GetMapping("/{ticker}/constituent")
    public ResponseEntity<List<ConstituentResponse>> getConstituents(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(etfService.getConstituents(partyId, ticker));
    }
}
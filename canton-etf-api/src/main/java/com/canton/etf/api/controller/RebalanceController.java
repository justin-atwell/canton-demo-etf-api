package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CreateRebalanceRequest;
import com.canton.etf.api.service.RebalanceService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etf/{ticker}/rebalance")
public class RebalanceController {

    private final RebalanceService rebalanceService;
    private final CantonPartyResolver cantonPartyResolver;

    public RebalanceController(RebalanceService rebalanceService, CantonPartyResolver cantonPartyResolver) {
        this.rebalanceService = rebalanceService;
        this.cantonPartyResolver = cantonPartyResolver;
    }

    @PostMapping
    public ResponseEntity<String> rebalance(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateRebalanceRequest request,
            @PathVariable String ticker) {
        String partyId = cantonPartyResolver.resolveParty(authHeader);
        String proposalId = rebalanceService.propose(partyId, ticker, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalId);
    }

    @GetMapping("/{proposalId}")
    public ResponseEntity<?> getProposal(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String ticker,
                                         @PathVariable String proposalId){
        String partyId = cantonPartyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(rebalanceService.getProposal(partyId, ticker, proposalId));
    }

    @PutMapping("/{proposalId}/approve")
    public ResponseEntity<?> approveProposal(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker,
            @PathVariable String proposalId){
        String partyId = cantonPartyResolver.resolveParty(authHeader);

        rebalanceService.approve(partyId,ticker,proposalId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{proposalId}/reject")
    public ResponseEntity<?> rejectProposal(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker,
            @PathVariable String proposalId){
        String partyId = cantonPartyResolver.resolveParty(authHeader);

        rebalanceService.reject(partyId,ticker,proposalId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{proposalId}/execute")
    public ResponseEntity<?> executeProposal(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker,
            @PathVariable String proposalId){
        String partyId = cantonPartyResolver.resolveParty(authHeader);

        rebalanceService.execute(partyId,ticker,proposalId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

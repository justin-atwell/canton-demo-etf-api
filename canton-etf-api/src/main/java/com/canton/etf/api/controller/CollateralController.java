package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CollateralTransactionRequest;
import com.canton.etf.api.dto.CreateCollateralAccountRequest;
import com.canton.etf.api.dto.LockCollateralRequest;
import com.canton.etf.api.dto.MarginCallRequest;
import com.canton.etf.api.service.CollateralService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collateral")
public class CollateralController {

    private final CollateralService collateralService;
    private final CantonPartyResolver partyResolver;

    public CollateralController(CollateralService collateralService, CantonPartyResolver partyResolver) {
        this.collateralService = collateralService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<String> createAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCollateralAccountRequest request) {

        String partyId = partyResolver.resolveParty(authHeader);
        String accountId = collateralService.createAccount(partyId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CollateralTransactionRequest request,
            @PathVariable String accountId) {

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.deposit(accountId, partyId, request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/lock")
    public ResponseEntity<?> lock(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LockCollateralRequest request,
            @PathVariable String accountId) {

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.lock(partyId, request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CollateralTransactionRequest request,
            @PathVariable String accountId) {

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.withdraw(partyId, request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/margincall")
    public ResponseEntity<?> issueMarginCall(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MarginCallRequest request,
            @PathVariable String accountId){

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.issueMarginCall(partyId, request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{accountId}/margincall/{callId}/meet")
    public ResponseEntity<?> meetMarginCall(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MarginCallRequest request,
            @PathVariable String accountId,
            @PathVariable String callId) {

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.meetMarginCall(partyId,callId,request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{accountId}/margincall/{callId}/default")
    public ResponseEntity<?> defaultMarginCall(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MarginCallRequest request,
            @PathVariable String accountId,
            @PathVariable String callId) {

        String partyId = partyResolver.resolveParty(authHeader);
        collateralService.defaultMarginCall(partyId,callId,request);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}

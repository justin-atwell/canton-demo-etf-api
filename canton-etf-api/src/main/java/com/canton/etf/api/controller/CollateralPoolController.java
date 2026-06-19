package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CollateralPoolDto;
import com.canton.etf.api.dto.CollateralPositionDto;
import com.canton.etf.api.service.CollateralPoolService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prime/pools")
public class CollateralPoolController {

    private final CollateralPoolService collateralPoolService;
    private final CantonPartyResolver partyResolver;

    public CollateralPoolController(CollateralPoolService collateralPoolService,
                                    CantonPartyResolver partyResolver) {
        this.collateralPoolService = collateralPoolService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<?> createPool(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CollateralPoolDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.createPool(partyId, request));
    }

    @GetMapping("/{poolId}")
    public ResponseEntity<?> getPool(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String poolId) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.getPool(partyId, poolId));
    }

    @GetMapping
    public ResponseEntity<?> getPools(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.getPools(partyId));
    }

    @PostMapping("/{poolId}/positions")
    public ResponseEntity<?> addPosition(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String poolId,
            @RequestBody CollateralPositionDto position) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.addPosition(partyId, poolId, position));
    }

    @DeleteMapping("/{poolId}/positions/{positionId}")
    public ResponseEntity<?> removePosition(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String poolId,
            @PathVariable String positionId) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.removePosition(partyId, poolId, positionId));
    }

    @PostMapping("/{poolId}/revalue")
    public ResponseEntity<?> revaluePool(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String poolId) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(collateralPoolService.revaluePool(partyId, poolId));
    }
}
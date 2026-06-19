package com.canton.etf.api.controller;

import com.canton.etf.api.dto.LiquidationWaterfallDto;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationPriority;
import com.canton.etf.api.service.LiquidationWaterfallService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prime/waterfalls")
public class LiquidationWaterfallController {

    private final LiquidationWaterfallService liquidationWaterfallService;
    private final CantonPartyResolver partyResolver;

    public LiquidationWaterfallController(LiquidationWaterfallService liquidationWaterfallService,
                                          CantonPartyResolver partyResolver) {
        this.liquidationWaterfallService = liquidationWaterfallService;
        this.partyResolver = partyResolver;
    }

    // PrimeBroker initiates — creates the LiquidationWaterfall contract
    @PostMapping
    public ResponseEntity<?> initiateWaterfall(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LiquidationWaterfallDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(liquidationWaterfallService.initiateWaterfall(partyId, request));
    }

    // PrimeBroker executes — runs priority sort + fold, archives pool atomically
    @PostMapping("/{id}/execute")
    public ResponseEntity<?> executeWaterfall(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody(required = false) List<LiquidationPriority> priorities) {
        String partyId = partyResolver.resolveParty(authHeader);
        List<LiquidationPriority> p = priorities != null ? priorities : List.of();
        return ResponseEntity.ok(liquidationWaterfallService.executeWaterfall(partyId, id, p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWaterfall(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(liquidationWaterfallService.getWaterfall(partyId, id));
    }

    // Returns immutable LiquidationAuditEvent records for this waterfall
    @GetMapping("/{id}/audit-events")
    public ResponseEntity<?> getAuditEvents(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(liquidationWaterfallService.getAuditEvents(partyId, id));
    }

    @GetMapping
    public ResponseEntity<?> getWaterfalls(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(liquidationWaterfallService.getWaterfalls(partyId));
    }
}
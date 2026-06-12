package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CollateralEligibilityDto;
import com.canton.etf.api.dto.EligibilityCheckRequestDto;
import com.canton.etf.api.service.CollateralEligibilityService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prime/eligibility")
public class CollateralEligibilityController {

    private final CollateralEligibilityService eligibilityService;
    private final CantonPartyResolver partyResolver;

    public CollateralEligibilityController(CollateralEligibilityService eligibilityService,
                                           CantonPartyResolver partyResolver) {
        this.eligibilityService = eligibilityService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CollateralEligibilityDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(eligibilityService.createSchedule(partyId, request));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getSchedule(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String scheduleId) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(eligibilityService.getSchedule(partyId, scheduleId));
    }

    @PostMapping("/{scheduleId}/check")
    public ResponseEntity<?> checkEligibility(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String scheduleId,
            @RequestBody EligibilityCheckRequestDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(eligibilityService.checkEligibility(partyId, scheduleId, request));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String scheduleId,
            @RequestBody CollateralEligibilityDto request) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(eligibilityService.updateSchedule(partyId, scheduleId, request));
    }
}